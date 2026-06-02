package org.example.corepayuserservice.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.corepayuserservice.global.domain.UserRole;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Entity
@Table(name = "auth_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser implements Persistable<Long> {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Transient
    private boolean isNew = true;

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Builder
    public AuthUser(Long id ,String email, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        // 권한이 안 들어오면 기본값으로 일반 유저(USER) 설정
        this.role = role != null ? role : UserRole.USER;
    }

    public boolean updatePassword(@NotNull String password){
        if(password != null && !Objects.equals(this.password, password)){
            this.password =password;
            return true;
        }
        return false;
    }


}