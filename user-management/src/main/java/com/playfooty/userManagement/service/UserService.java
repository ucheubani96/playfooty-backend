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

        UserProfile userProfile = new UserProfile();

        String hes = userProfile.getUsername();

//        userProfile.setId(id);
//        userProfile.setEmail(request.getEmail());
//        userProfile.setUsername(request.getUsername());
//        userProfile.setImage(request.getImage());

        return userProfileRepo.saveAndFlush(userProfile);
    }

    public Boolean verifyUsernameUniqueness (String username) {
        return userProfileRepo.existsByUsername(username);
    }

    public Boolean verifyEmailUniqueness (String email) {
        return userProfileRepo.existsByEmail(email);
    }
}
