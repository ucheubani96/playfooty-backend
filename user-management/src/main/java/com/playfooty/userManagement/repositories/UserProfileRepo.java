package com.playfooty.userManagement.repositories;

import com.playfooty.userManagement.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, UUID> {
}
