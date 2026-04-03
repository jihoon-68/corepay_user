package org.example.corepayuserservice.user.presentation.dto.req;

import org.example.corepayuserservice.user.domain.UserRole;

public record UserUpdateRole(
        Long id,
        UserRole role
) {
}
