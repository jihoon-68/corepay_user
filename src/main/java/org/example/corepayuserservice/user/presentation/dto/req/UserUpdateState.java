package org.example.corepayuserservice.user.presentation.dto.req;

import org.example.corepayuserservice.user.domain.UserState;

public record UserUpdateState(
        Long id,
        UserState state
) {
}
