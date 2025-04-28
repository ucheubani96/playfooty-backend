package com.playfooty.userManagement.model;

import com.playfooty.backendCore.model.Auditable;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
