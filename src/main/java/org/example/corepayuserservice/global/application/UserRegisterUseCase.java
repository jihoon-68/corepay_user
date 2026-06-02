package org.example.corepayuserservice.global.application;

import lombok.RequiredArgsConstructor;
import org.example.corepayuserservice.auth.application.AuthService;
import org.example.corepayuserservice.auth.application.command.SignupCommand;
import org.example.corepayuserservice.user.application.UserService;
import org.example.corepayuserservice.user.application.command.CreateUserCommand;
import org.example.corepayuserservice.auth.presentation.res.AuthUserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegisterUseCase {

    private final AuthService authService;
    private final UserService userService;

    @Transactional
    public void register(CreateUserCommand command){
        AuthUserDto user = userService.creat(command);

        SignupCommand authCommand = SignupCommand.builder()
                .id(user.id())
                .email(user.email())
                .password(user.password())
                .role(user.role())
                .build();

        authService.signup(authCommand);
    }


}
