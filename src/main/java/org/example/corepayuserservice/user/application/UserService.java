package org.example.corepayuserservice.user.application;

import org.example.corepayuserservice.user.application.command.CreateUserCommand;
import org.example.corepayuserservice.user.application.command.UpdateUserInfoCommand;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateRole;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateState;
import org.example.corepayuserservice.auth.presentation.res.AuthUserDto;
import org.example.corepayuserservice.user.presentation.dto.res.UserDto;

import java.util.List;

public interface UserService {

    AuthUserDto creat(CreateUserCommand command);
    UserDto updateInfo(UpdateUserInfoCommand command);
    void updateState(UserUpdateState updateState);
    void updateRole(UserUpdateRole updateRole);
    UserDto get(Long id);
    List<UserDto> getList();
    void delete(Long id);
}
