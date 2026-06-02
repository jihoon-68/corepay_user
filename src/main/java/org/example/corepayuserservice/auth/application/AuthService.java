package org.example.corepayuserservice.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.corepayuserservice.auth.application.command.LoginCommand;
import org.example.corepayuserservice.auth.application.command.SignupCommand;
import org.example.corepayuserservice.auth.application.command.UpdatePasswordCommand;
import org.example.corepayuserservice.auth.domain.AuthUser;
import org.example.corepayuserservice.auth.infrastructure.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface{

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //로그인 로직: 이메일과 비밀번호를 검증하고 JWT를 반환합니다.
    @Transactional(readOnly = true)
    @Override
    public String login(LoginCommand command) {
        // 1. 이메일로 유저 찾기
        AuthUser authUser = getAuthUser(command.email());
        // 2. 비밀번호 일치 여부 확인 (평문 비밀번호와 DB의 해시된 비밀번호 비교)

        if (!passwordEncoder.matches(command.password(), authUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 검증 성공 시 JWT 토큰 발급 (유저 ID, 이메일, 권한 전달)
        String token = jwtProvider.generateToken(
                authUser.getId(),
                authUser.getEmail(),
                authUser.getRole().name()
        );

        log.info("로그인 성공 및 토큰 발급 완료 - UserID: {}", authUser.getId());
        return token;
    }

    @Transactional
    @Override
    public void signup(SignupCommand command){
        AuthUser authUser = AuthUser.builder()
                .id(command.id())
                .email(command.email())
                .password(command.password())
                .role(command.role())
                .build();

        authUserRepository.save(authUser);
    }

    @Transactional
    @Override
    public void updatePassword(UpdatePasswordCommand command) {
        AuthUser authUser = getAuthUser(command.email());
        if (passwordEncoder.matches(command.password(), authUser.getPassword())) {
            throw new IllegalArgumentException("이전 비밀번호와 동일한 비밀번호입니다.");
        }
        authUser.updatePassword(passwordEncoder.encode(command.password()));
        authUserRepository.saveAndFlush(authUser);
    }

    private AuthUser getAuthUser(String email){
        return authUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
    }
}
