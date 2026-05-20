package org.example.corepayuserservice.user.application;

import lombok.RequiredArgsConstructor;
import org.example.corepayuserservice.user.application.command.CreateUserCommand;
import org.example.corepayuserservice.user.application.command.UpdateUserInfoCommand;
import org.example.corepayuserservice.user.infrastructure.kafka.event.UserCreatedEvent;
import org.example.corepayuserservice.user.infrastructure.kafka.event.UserUpdatePasswordEvent;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateRole;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateState;
import org.example.corepayuserservice.user.presentation.dto.res.UserDto;
import org.example.corepayuserservice.user.domain.User;
import org.example.corepayuserservice.user.infrastructure.db.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public UserDto creat(CreateUserCommand command) {
        User user = userRepository.findByEmail(command.email());
        if (user != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + command.email());
        }

        String encodedPassword = passwordEncoder.encode(command.password());

        User newUser = User.builder()
                .name(command.name())
                .email(command.email())
                .password(encodedPassword)
                .role(command.role())
                .build();

        User savedUser = userRepository.save(newUser);

        publisher.publishEvent(
                UserCreatedEvent.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .role(newUser.getRole())
                .build()
        );

        return UserDto.from(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateInfo(UpdateUserInfoCommand command) {
        User user = userRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        String password = passwordEncoder.encode(command.password());
        user.updateInfo(command.name(), command.email());

        if(user.updatePassword(password)){
            UserUpdatePasswordEvent event = UserUpdatePasswordEvent.builder()
                    .email(command.email())
                    .password(password)
                    .build();
            publisher.publishEvent(event);
        }

        userRepository.save(user);

        return UserDto.from(user);
    }

    @Override
    @Transactional
    public void updateState(UserUpdateState req) {
        User user = userRepository.findById(req.id()).orElseThrow();
        user.updateState(req.state());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateRole(UserUpdateRole req) {
        User user = userRepository.findById(req.id()).orElseThrow();
        user.updateRole(req.role());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return UserDto.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getList() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
