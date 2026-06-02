package org.example.corepayuserservice.user.presentation.dto.res;

import lombok.Builder;
import org.example.corepayuserservice.user.domain.User;
import org.example.corepayuserservice.global.domain.UserRole;
import org.example.corepayuserservice.user.domain.UserState;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        Long id,
        String name,
        String email,
        UserRole role,
        UserState state,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .state(user.getState())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
