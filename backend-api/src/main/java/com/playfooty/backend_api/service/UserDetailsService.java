package com.playfooty.backend_api.service;

import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backend_api.model.UserDetails;
import com.playfooty.backend_api.repository.UserDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserDetailsRepo userDetailsRepo;

    public UserDetails addUserDetails (AddUserRequestDto addUserRequest) throws RuntimeException {
        UserDetails userDetails = UserDetails.builder()
                .email(addUserRequest.getEmail())
                .password(addUserRequest.getPassword())
                .username(addUserRequest.getUsername())
                .build();

        return userDetailsRepo.saveAndFlush(userDetails);
    }

    public Optional<UserDetails> findById (UUID id) throws RuntimeException {
        return userDetailsRepo.findById(id);
    }

    public Boolean verifyUsernameUniqueness (String username) {
        return userDetailsRepo.existsByUsername(username);
    }

    public Boolean verifyEmailUniqueness (String email) {
        return userDetailsRepo.existsByEmail(email);
    }
}
