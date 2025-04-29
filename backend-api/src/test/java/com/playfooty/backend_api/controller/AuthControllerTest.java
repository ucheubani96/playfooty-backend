package com.playfooty.backend_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playfooty.backend_api.service.AuthService;
import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.dto.SuccessResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper; // to serialize AddUserRequestDto to JSON

    @Test
    void signup_ShouldReturnSuccessResponse() throws Exception {

        AddUserRequestDto requestDto = new AddUserRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setEmail("testuser@example.com");
        requestDto.setPassword("password123");

        Mockito.doNothing().when(authService).register(Mockito.any(AddUserRequestDto.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Signup successful. Check your email for confirmation"));

        Mockito.verify(authService).register(Mockito.any(AddUserRequestDto.class));
    }

    @Test
    void signup_ShouldReturnBadRequest_WhenUsernameIsMissing() throws Exception {
        AddUserRequestDto requestDto = new AddUserRequestDto();
        requestDto.setEmail("testuser@example.com");
        requestDto.setPassword("password123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        AddUserRequestDto requestDto = new AddUserRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setEmail("not-an-email"); // Invalid email
        requestDto.setPassword("password123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_ShouldReturnBadRequest_WhenPasswordIsMissing() throws Exception {
        AddUserRequestDto requestDto = new AddUserRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setEmail("testuser@example.com");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

}
