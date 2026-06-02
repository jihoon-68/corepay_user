package org.example.corepayuserservice.user.presentation.dto.req;

import org.example.corepayuserservice.global.domain.UserRole;

public record UserUpdateRole(
        Long id,
        UserRole role
) {
}
