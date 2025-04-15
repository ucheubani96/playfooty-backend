package com.playfooty.backend_api.service;

import com.playfooty.backendCore.exception.BadRequestException;
import com.playfooty.backendCore.service.BaseService;
import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backend_api.model.UserDetails;
import com.playfooty.backend_api.repository.UserDetailsRepo;
import com.playfooty.userManagement.model.UserProfile;
import com.playfooty.userManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService {

    private final UserService userService;

    private final UserDetailsRepo userDetailsRepo;

    public void register (AddUserRequestDto request) throws RuntimeException {

        if (verifyUsernameUniqueness(request.getUsername())) throw new BadRequestException("Username already exist");

        if (verifyEmailUniqueness(request.getEmail())) throw new BadRequestException("Email already exist");

        request.setPassword(hashService.generate(request.getPassword()));

        UserDetails userDetails = addUserDetails(request);

        UserProfile user = userService.addUser(request, userDetails.getId());

//        Send welcome email
    }

    private UserDetails addUserDetails (AddUserRequestDto addUserRequest) throws RuntimeException {
        UserDetails userDetails = UserDetails.builder()
                .email(addUserRequest.getEmail())
                .password(addUserRequest.getPassword())
                .username(addUserRequest.getUsername())
                .build();

        return userDetailsRepo.saveAndFlush(userDetails);
    }

    public Boolean verifyUsernameUniqueness (String username) {
        return userDetailsRepo.existsByUsername(username);
    }

    public Boolean verifyEmailUniqueness (String email) {
        return userDetailsRepo.existsByEmail(email);
    }
}
