package org.example.corepayuserservice.auth.infrastructure;

import org.example.corepayuserservice.auth.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByEmail(String email);

}
