package org.example.corepayuserservice.user.presentation.dto.req;


public record UserCreatReq(
        String name,
        String email,
        String password,
        String role
) {
}
