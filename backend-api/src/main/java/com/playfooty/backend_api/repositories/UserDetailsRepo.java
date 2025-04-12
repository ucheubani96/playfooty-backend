package com.playfooty.backend_api.repositories;

import com.playfooty.backend_api.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDetailsRepo extends JpaRepository<UUID, UserDetails> {
}
