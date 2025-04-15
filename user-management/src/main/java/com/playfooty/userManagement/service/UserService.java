package com.playfooty.userManagement.service;

import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.service.BaseService;
import com.playfooty.userManagement.model.UserProfile;
import com.playfooty.userManagement.repository.UserProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserProfileRepo userProfileRepo;

    public UserProfile addUser (AddUserRequestDto request, UUID id) throws RuntimeException {

        UserProfile user = UserProfile.builder()
                .id(id)
                .email(request.getEmail())
                .username(request.getUsername())
                .image(request.getImage())
                .build();

        return userProfileRepo.saveAndFlush(user);
    }

    public Boolean verifyUsernameUniqueness (String username) {
        return userProfileRepo.existsByUsername(username);
    }

    public Boolean verifyEmailUniqueness (String email) {
        return userProfileRepo.existsByEmail(email);
    }
}
