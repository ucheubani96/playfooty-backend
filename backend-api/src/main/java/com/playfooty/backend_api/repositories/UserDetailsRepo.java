package com.playfooty.backend_api.repositories;

import com.playfooty.backend_api.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails, UUID> {
}
