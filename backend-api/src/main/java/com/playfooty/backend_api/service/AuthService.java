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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService extends BaseService {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final PasswordHasher passwordHasher;
    private final JWTService jwtService;

    public void register(AddUserRequestDto request) {
        log.info("Starting registration for username='{}', email='{}'", request.getUsername(), request.getEmail());

        if (userDetailsService.verifyUsernameUniqueness(request.getUsername())) {
            log.warn("Username already exists: {}", request.getUsername());
            throw new BadRequestException("Username already exist");
        }

        if (userDetailsService.verifyEmailUniqueness(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new BadRequestException("Email already exist");
        }

        String rawPwd = request.getPassword();
        String hashed = passwordHasher.hashPassword(rawPwd);
        request.setPassword(hashed);
        log.debug("Password hashed for username='{}'", request.getUsername());

        UserDetails userDetails = userDetailsService.addUserDetails(request);
        log.info("Created UserDetails id='{}' for username='{}'", userDetails.getId(), request.getUsername());

        UserProfile profile = userService.addUser(request, userDetails.getId());
        log.info("Created UserProfile id='{}' for UserDetails id='{}'", profile.getId(), userDetails.getId());

        log.info("Registration completed for username='{}'", request.getUsername());
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Attempting login for email='{}'", request.getEmail());

        UserDetails userDetails = userDetailsService.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: email not found '{}'", request.getEmail());
                    return new BadCredentialsException("Invalid credentials");
                });

        if (!passwordHasher.matches(request.getPassword(), userDetails.getPassword())) {
            log.warn("Login failed: bad password for email='{}'", request.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!userDetails.getIsActive()) {
            log.warn("Login failed: account inactive for userId='{}'", userDetails.getId());
            throw new UnauthorizedException("Account inactive");
        }

        // Build claims and generate token
        HashMap<String, String> claims = new HashMap<>();
        claims.put("id", userDetails.getId().toString());
        String token = jwtService.generate(claims);
        log.info("Login successful for userId='{}', issuing token", userDetails.getId());

        return new LoginResponseDTO(token);
    }
}
