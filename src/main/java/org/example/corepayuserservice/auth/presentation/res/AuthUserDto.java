package org.example.corepayuserservice.auth.presentation.res;

import lombok.Builder;
import org.example.corepayuserservice.global.domain.UserRole;
import org.example.corepayuserservice.user.domain.User;

@Builder
public record AuthUserDto(
        Long id,
        String name,
        String email,
        UserRole role,
        String password
) {
    public static AuthUserDto from (User user){
        return AuthUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
