package com.playfooty.backend_api.controller;

import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.dto.SuccessResponseDTO;
import com.playfooty.backend_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> signup(@Valid @RequestBody AddUserRequestDto userData) {

        authService.register(userData);

        SuccessResponseDTO response = SuccessResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Signup successful. Check your email for confirmation")
                .build();

        return ResponseEntity.ok(response);
    }
}
