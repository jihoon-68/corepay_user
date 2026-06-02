package org.example.corepayuserservice.user.application.command;

import lombok.Builder;
import org.example.corepayuserservice.global.domain.UserRole;

@Builder
public record CreateUserCommand(
        String name,
        String email,
        String password,
        UserRole role
) {
}
