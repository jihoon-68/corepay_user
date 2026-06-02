package org.example.corepayuserservice;

import org.example.corepayuserservice.user.application.BasicUserService;
import org.example.corepayuserservice.user.application.command.CreateUserCommand;
import org.example.corepayuserservice.user.application.command.UpdateUserInfoCommand;
import org.example.corepayuserservice.user.domain.User;
import org.example.corepayuserservice.global.domain.UserRole;
import org.example.corepayuserservice.user.infrastructure.db.UserRepository;
import org.example.corepayuserservice.user.presentation.dto.res.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, ports = {9092})
public class BasicUserServiceIntegrationTest {

    @Autowired
    private BasicUserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private CountDownLatch latch;
    private String receivedMessage;
    private CreateUserCommand defaultCommand;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        latch = new CountDownLatch(1);
        receivedMessage = null;

        // 고정 테스트 사용자 커맨드 셋팅
        defaultCommand = CreateUserCommand.builder()
                .name("박지훈")
                .email("jihoon@example.com")
                .password("securePassword123")
                .role(UserRole.valueOf("USER"))
                .build();

        // 내장 카프카 파티션 할당 대기 안정화 코드
        for (MessageListenerContainer container : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(container, 1);
        }
    }

    @AfterEach
    void tearDown() {
        // 테스트 완료 후 컨슈머 종료 및 DB 데이터 클리닝
        userRepository.deleteAll();
    }

    // 오더 서버와 동일한 방식의 테스트용 인라인 카프카 리스너 등록
    @KafkaListener(topics = "user-created-topic", groupId = "test-auth-group")
    public void listen(String message) {
        this.receivedMessage = message;
        this.latch.countDown();
    }

    // 비밀번호 변경 이벤트를 수신할 새로운 리스너 추가
    @KafkaListener(topics = "user-update-password-topic", groupId = "test-auth-group")
    public void listenPasswordUpdate(String message) {
        this.receivedMessage = message;
        this.latch.countDown();
    }

    @Test
    @DisplayName("유저 회원가입 시 DB에 암호화되어 저장되고, 암호화된 비밀번호가 담긴 카프카 이벤트를 발행한다.")
    void createUser_Success_EmbedKafkaVerify() throws Exception {
        // When: 유저 생성(회원가입) 비즈니스 로직 수행
        UserDto result = userService.creat(defaultCommand);

        // Then 1: 카프카 이벤트 발행 검증 (비동기 대기)
        boolean messageReceived = latch.await(5, TimeUnit.SECONDS);

        assertThat(messageReceived).isTrue();
        // 암호화된 비밀번호와 이메일이 메시지 바디에 직렬화되어 꽂혔는지 검증
        User savedUser = userRepository.findById(result.id()).orElseThrow();
        assertThat(receivedMessage).contains(savedUser.getPassword());
        assertThat(receivedMessage).contains("jihoon@example.com");

        // Then 2: DB 저장 및 패스워드 해싱 상태 최종 검증
        assertThat(savedUser.getEmail()).isEqualTo("jihoon@example.com");
        assertThat(passwordEncoder.matches("securePassword123", savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("유저 정보 업데이트 시 비밀번호가 변경되면 카프카 이벤트를 발행한다.")
    void updateInfo_PasswordChanged_EmbedKafkaVerify() throws Exception {
        // Given: 기존 유저를 DB에 미리 세팅
        User savedUser = userRepository.save(User.builder()
                .name("박지훈")
                .email("jihoon@example.com")
                .password(passwordEncoder.encode("oldPassword123"))
                .role(UserRole.valueOf("USER"))
                .build());

        UpdateUserInfoCommand command = new UpdateUserInfoCommand(
                savedUser.getId(),
                "박지훈",
                "jihoon@example.com",
                "newSecurePassword456" // 새로운 비밀번호
        );

        // When: 정보 업데이트 로직 수행
        userService.updateInfo(command);

        // Then: 카프카 이벤트 발행 검증 (비동기 대기)
        boolean messageReceived = latch.await(5, TimeUnit.SECONDS);

        assertThat(messageReceived).isTrue();

        // DB에 저장된 암호화된 새 비밀번호와 카프카 메시지로 날아간 비밀번호가 일치하는지 검증
        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(receivedMessage).contains(updatedUser.getPassword());
        assertThat(receivedMessage).contains("jihoon@example.com");
    }
}