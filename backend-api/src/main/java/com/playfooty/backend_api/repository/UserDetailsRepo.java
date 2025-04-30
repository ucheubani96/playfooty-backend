package com.playfooty.backend_api.repository;

import com.playfooty.backend_api.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails, UUID> {
    @Query("select u from UserDetails u where upper(u.email) = upper(?1)")
    Optional<UserDetails> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
