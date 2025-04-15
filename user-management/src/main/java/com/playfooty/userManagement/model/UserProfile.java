package com.playfooty.userManagement.model;

import jakarta.persistence.*;

import java.util.UUID;

import com.playfooty.backendCore.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "user_profiles")
public class UserProfile extends Auditable {
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "image", nullable = false, unique = true, length = 1000)
    private String image;
}
