package com.playfooty.backend_api.service;

import com.playfooty.backendCore.service.BaseService;
import com.playfooty.backend_api.dto.AddUserRequestDto;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService {
    public void register (AddUserRequestDto userData) throws RuntimeException {

        if (userService.verifyUsernameUniqueness(userData.getUsername())) throw new BadRequestException("Username already exist");

        if (userService.verifyEmailUniqueness(userData.getEmail())) throw new BadRequestException("Email already exist");

        userData.setPassword(hashService.generate(userData.getPassword()));

        User user = userService.add(userData);

//        Send welcome email
    }
}
