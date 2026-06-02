package org.example.corepayuserservice.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.example.corepayuserservice.auth.application.AuthService;
import org.example.corepayuserservice.auth.application.command.LoginCommand;
import org.example.corepayuserservice.auth.application.command.UpdatePasswordCommand;
import org.example.corepayuserservice.auth.presentation.req.LoginRequest;
import org.example.corepayuserservice.auth.presentation.req.UpdatePasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //로그인 API: POST /api/auth/login

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {

        LoginCommand command = LoginCommand.builder()
                .email(request.email())
                .password(request.password())
                .build();

        // 서비스에서 로그인 검증 및 토큰 발급
        String token = authService.login(command);

        // 클라이언트에게 "Bearer {토큰}" 형식으로 반환 (또는 Authorization 헤더에 담아도 됩니다)
        return ResponseEntity.ok(Map.of("accessToken", "Bearer " + token));
    }

    @PostMapping("/update_password")
    public ResponseEntity<Void> UpdatePassword(@RequestBody UpdatePasswordRequest request){
        UpdatePasswordCommand command = UpdatePasswordCommand.builder()
                .email(request.email())
                .password(request.password())
                .build();

        authService.updatePassword(command);

        return ResponseEntity.noContent().build();
    }

}