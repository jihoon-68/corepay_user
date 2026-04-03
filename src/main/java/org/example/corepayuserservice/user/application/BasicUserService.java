package org.example.corepayuserservice.user.application;

import lombok.RequiredArgsConstructor;
import org.example.corepayuserservice.user.presentation.dto.req.UserCreatReq;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateInfoReq;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateRole;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateState;
import org.example.corepayuserservice.user.presentation.dto.res.UserDto;
import org.example.corepayuserservice.user.domain.User;
import org.example.corepayuserservice.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto creat(UserCreatReq req) {
        User user = userRepository.findByEmail(req.email());
        if (user != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + req.email());
        }

        User newUser = User.builder()
                .name(req.name())
                .email(req.email())
                .role(req.role())
                .build();

        User savedUser = userRepository.save(newUser);
        return UserDto.from(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateInfo(UserUpdateInfoReq req) {
        User user = userRepository.findById(req.id())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.updateInfo(req.name(), req.email());
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
