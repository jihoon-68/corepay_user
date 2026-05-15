package org.example.corepayuserservice.user.presentation;

import lombok.RequiredArgsConstructor;
import org.example.corepayuserservice.user.application.command.CreateUserCommand;
import org.example.corepayuserservice.user.application.command.UpdateUserInfoCommand;
import org.example.corepayuserservice.user.presentation.dto.req.UserCreatReq;
import org.example.corepayuserservice.user.presentation.dto.req.UserUpdateInfoReq;
import org.example.corepayuserservice.user.presentation.dto.res.UserDto;
import org.example.corepayuserservice.user.application.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreatReq req) {
        CreateUserCommand command = CreateUserCommand.builder()
                .name(req.name())
                .email(req.email())
                .role(req.role())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.creat(command));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyInfo(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    //전체 유저 목록 (관리자 전용)
    @GetMapping
    public ResponseEntity<List<UserDto>> getUserList(@RequestHeader("X-User-Id") Long adminId) {
        return ResponseEntity.ok(userService.getList());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateMyInfo(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UserUpdateInfoReq req)
    {
        UpdateUserInfoCommand command = UpdateUserInfoCommand.builder()
                .id(userId)
                .name(req.name())
                .email(req.email())
                .build();

        return ResponseEntity.ok(userService.updateInfo(command));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@RequestHeader("X-User-Id") Long userId) {
        userService.delete(userId); // 본인 계정 삭제
        return ResponseEntity.noContent().build();
    }


}
