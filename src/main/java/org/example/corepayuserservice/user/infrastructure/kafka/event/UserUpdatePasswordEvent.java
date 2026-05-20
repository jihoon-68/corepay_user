package org.example.corepayuserservice.user.infrastructure.kafka.event;

import lombok.Builder;

@Builder
public record UserUpdatePasswordEvent(
        String email,
        String password
) {
}
