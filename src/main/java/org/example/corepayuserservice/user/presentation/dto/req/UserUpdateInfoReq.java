package org.example.corepayuserservice.user.presentation.dto.req;

public record UserUpdateInfoReq(
        Long id,
        String name,
        String email
) {
}
