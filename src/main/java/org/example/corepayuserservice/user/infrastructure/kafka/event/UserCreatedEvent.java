package org.example.corepayuserservice.user.infrastructure.kafka.event;

import lombok.Builder;
import org.example.corepayuserservice.user.domain.UserRole;

@Builder
public record UserCreatedEvent(
        Long id,
        String email,
        String password,
        UserRole role
) {
}
