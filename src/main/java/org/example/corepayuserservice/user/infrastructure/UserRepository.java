package org.example.corepayuserservice.user.infrastructure;

import org.example.corepayuserservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String name);
}
