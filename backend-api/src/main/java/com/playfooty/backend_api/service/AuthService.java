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

    private final UserDetailsService userDetailsService;

    private final PasswordHasher passwordHasher;

    public void register (AddUserRequestDto request) throws RuntimeException {

        if (userDetailsService.verifyUsernameUniqueness(request.getUsername())) throw new BadRequestException("Username already exist");

        if (userDetailsService.verifyEmailUniqueness(request.getEmail())) throw new BadRequestException("Email already exist");

        request.setPassword(passwordHasher.hashPassword(request.getPassword()));

        UserDetails userDetails = userDetailsService.addUserDetails(request);

        UserProfile user = userService.addUser(request, userDetails.getId());

//        Send welcome email
    }


}
