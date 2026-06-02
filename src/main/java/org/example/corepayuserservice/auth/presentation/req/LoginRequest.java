package org.example.corepayuserservice.auth.presentation.req;

import lombok.Builder;

@Builder
public record LoginRequest(
        String email,
        String password
) {}