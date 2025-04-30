package com.playfooty.backend_api.controller;

import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.dto.SuccessResponseDTO;
import com.playfooty.backend_api.dto.LoginRequestDTO;
import com.playfooty.backend_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> signup(@Valid @RequestBody AddUserRequestDto userData) {
        log.info("POST /auth/register ▶ start; username='{}', email='{}'",
                userData.getUsername(), userData.getEmail());

        authService.register(userData);

        SuccessResponseDTO response = SuccessResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Signup successful. Check your email for confirmation")
                .build();

        log.info("POST /auth/register ◀ success; username='{}'", userData.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginData) {
        log.info("POST /auth/login ▶ start; email='{}'", loginData.getEmail());

        authService.login(loginData);

        SuccessResponseDTO response = SuccessResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Login successful.")
                .build();

        log.info("POST /auth/login ◀ success; email='{}'", loginData.getEmail());
        return ResponseEntity.ok(response);
    }
}
