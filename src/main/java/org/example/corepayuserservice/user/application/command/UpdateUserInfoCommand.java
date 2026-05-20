package org.example.corepayuserservice.user.application.command;

import lombok.Builder;

@Builder
public record UpdateUserInfoCommand(
        Long id,
        String name,
        String email,
        String password
){
}
