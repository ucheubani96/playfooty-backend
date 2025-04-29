package com.playfooty.backend_api.service;

import com.playfooty.backendCore.exception.BadRequestException;
import com.playfooty.backendCore.exception.UnauthorizedException;
import com.playfooty.backendCore.service.BaseService;
import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backend_api.dto.LoginRequestDTO;
import com.playfooty.backend_api.dto.LoginResponseDTO;
import com.playfooty.backend_api.model.UserDetails;
import com.playfooty.backend_api.security.JWTService;
import com.playfooty.userManagement.model.UserProfile;
import com.playfooty.userManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final PasswordHasher passwordHasher;

    private final JWTService jwtService;

    public void register (AddUserRequestDto request) throws RuntimeException {

        if (userDetailsService.verifyUsernameUniqueness(request.getUsername())) throw new BadRequestException("Username already exist");

        if (userDetailsService.verifyEmailUniqueness(request.getEmail())) throw new BadRequestException("Email already exist");

        request.setPassword(passwordHasher.hashPassword(request.getPassword()));

        UserDetails userDetails = userDetailsService.addUserDetails(request);

        UserProfile user = userService.addUser(request, userDetails.getId());

//        Send welcome email
    }

    public LoginResponseDTO login (LoginRequestDTO request) throws RuntimeException {
        UserDetails userDetails = userDetailsService.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordHasher.matches(request.getPassword(), userDetails.getPassword())) throw new BadCredentialsException("Invalid credentials");
        if (!userDetails.getIsActive()) throw new UnauthorizedException("Account inactive");

        HashMap<String, String> claims = new HashMap<String, String>();
        claims.put("id", userDetails.getId().toString());


        String token = jwtService.generate(claims);

        return new LoginResponseDTO(token);
    }

}
