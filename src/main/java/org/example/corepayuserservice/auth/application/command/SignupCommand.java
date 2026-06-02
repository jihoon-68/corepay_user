package org.example.corepayuserservice.auth.application.command;

import lombok.Builder;
import org.example.corepayuserservice.global.domain.UserRole;


@Builder
public record SignupCommand(
        Long id,
        String email,
        String password,
        UserRole role
) {

}
