package org.example.corepayuserservice.auth.application.command;

import lombok.Builder;

@Builder
public record UpdatePasswordCommand(
        String email,
        String password
) {
}
