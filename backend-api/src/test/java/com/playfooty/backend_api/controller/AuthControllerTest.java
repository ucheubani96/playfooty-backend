package com.playfooty.backend_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playfooty.backend_api.dto.LoginRequestDTO;
import com.playfooty.backend_api.service.AuthService;
import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.dto.SuccessResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /auth/register")
    class RegisterTests {

        @Test
        @DisplayName("should return 200 with success message for valid signup")
        void signup_ShouldReturnSuccessResponse() throws Exception {
            AddUserRequestDto requestDto = new AddUserRequestDto();
            requestDto.setUsername("testuser");
            requestDto.setEmail("testuser@example.com");
            requestDto.setPassword("password123");

            Mockito.doNothing().when(authService).register(any(AddUserRequestDto.class));

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message")
                            .value("Signup successful. Check your email for confirmation"));

            Mockito.verify(authService).register(any(AddUserRequestDto.class));
        }

        @Test
        @DisplayName("should return 400 when username is missing")
        void signup_ShouldReturnBadRequest_WhenUsernameIsMissing() throws Exception {
            AddUserRequestDto requestDto = new AddUserRequestDto();
            requestDto.setEmail("testuser@example.com");
            requestDto.setPassword("password123");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when email is invalid")
        void signup_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
            AddUserRequestDto requestDto = new AddUserRequestDto();
            requestDto.setUsername("testuser");
            requestDto.setEmail("not-an-email");
            requestDto.setPassword("password123");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when password is missing")
        void signup_ShouldReturnBadRequest_WhenPasswordIsMissing() throws Exception {
            AddUserRequestDto requestDto = new AddUserRequestDto();
            requestDto.setUsername("testuser");
            requestDto.setEmail("testuser@example.com");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /auth/login")
    class LoginTests {

        @Test
        @DisplayName("should return 200 with success message for valid login")
        void login_ShouldReturnSuccessResponse() throws Exception {
            LoginRequestDTO requestDto = new LoginRequestDTO();
            requestDto.setEmail("testuser@example.com");
            requestDto.setPassword("password123");

            // authService.login returns a DTO, but controller ignores it—just verify it's called
            Mockito.doNothing().when(authService).login(any(LoginRequestDTO.class));

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Login successful."));

            Mockito.verify(authService).login(any(LoginRequestDTO.class));
        }

        @Test
        @DisplayName("should return 400 when email is missing")
        void login_ShouldReturnBadRequest_WhenEmailIsMissing() throws Exception {
            LoginRequestDTO requestDto = new LoginRequestDTO();
            requestDto.setPassword("password123");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when email is invalid")
        void login_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
            LoginRequestDTO requestDto = new LoginRequestDTO();
            requestDto.setEmail("not-an-email");
            requestDto.setPassword("password123");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when password is missing")
        void login_ShouldReturnBadRequest_WhenPasswordIsMissing() throws Exception {
            LoginRequestDTO requestDto = new LoginRequestDTO();
            requestDto.setEmail("testuser@example.com");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());
        }
    }
}
