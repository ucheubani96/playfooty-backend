package com.playfooty.backend_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;
import com.playfooty.backendCore.model.Auditable;
import com.playfooty.userManagement.models.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "user_details")
public class UserDetails extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 1000)
    @JsonIgnore
    private String password;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private UserProfile user;
}
