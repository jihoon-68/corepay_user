package org.example.corepayuserservice.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserState state;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Builder
    public User (String name, String email, UserRole role){
        this.name = name;
        this.email = email;
        this.role = role;
        this.state = UserState.ACTIVE;
    }

    public void updateInfo(String name, String email){
        if(name != null && !Objects.equals(this.name, name)){
            this.name = name;
        }
        if(email != null && !Objects.equals(this.email, email)){
            this.email = email;
        }
    }

    public void updateRole(@NotNull UserRole role){
        this.role = role;
    }

    public void updateState(@NotNull UserState state){
        this.state = state;
    }
}
