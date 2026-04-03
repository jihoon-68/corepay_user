package org.example.corepayuserservice.user.presentation;

import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.creat(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUserList() {
        return ResponseEntity.ok(userService.getList());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody UserUpdateInfoReq req) {
        return ResponseEntity.ok(userService.updateInfo(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}
