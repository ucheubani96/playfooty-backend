package com.playfooty.userManagement.repositories;

import com.playfooty.userManagement.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepo extends JpaRepository<UUID, UserProfile> {
}
