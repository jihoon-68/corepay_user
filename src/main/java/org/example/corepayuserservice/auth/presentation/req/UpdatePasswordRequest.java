package org.example.corepayuserservice.auth.presentation.req;

import lombok.Builder;

@Builder
public record UpdatePasswordRequest(
        String email,
        String password
) {
}
