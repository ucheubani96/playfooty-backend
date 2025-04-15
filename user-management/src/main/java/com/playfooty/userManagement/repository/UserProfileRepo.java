package com.playfooty.userManagement.repository;

import com.playfooty.userManagement.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, UUID> {
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
