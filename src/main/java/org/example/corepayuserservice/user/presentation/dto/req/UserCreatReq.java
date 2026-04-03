package org.example.corepayuserservice.user.presentation.dto.req;

import org.example.corepayuserservice.user.domain.UserRole;

public record UserCreatReq(
        String name,
        String email,
        UserRole role
) {
}
