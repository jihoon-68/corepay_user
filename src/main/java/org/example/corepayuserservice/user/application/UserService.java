package org.example.corepayuserservice.user.application;

import org.example.corepayuserservice.user.presentation.dto.req.UserCreatReq;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateInfoReq;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateRole;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateState;
import org.example.corepayuserservice.user.presentation.dto.res.UserDto;

import java.util.List;

public interface UserService {

    UserDto creat(UserCreatReq req);
    UserDto updateInfo(UserUpdateInfoReq req);
    void updateState(UserUpdateState updateState);
    void updateRole(UserUpdateRole updateRole);
    UserDto get(Long id);
    List<UserDto> getList();
    void delete(Long id);
}
