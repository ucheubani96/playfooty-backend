package com.playfooty.backend_api.service;

import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.exception.BadRequestException;
import com.playfooty.backend_api.model.UserDetails;
import com.playfooty.userManagement.model.UserProfile;
import com.playfooty.userManagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_successfulRegistration() {
        // Arrange
        AddUserRequestDto request = new AddUserRequestDto();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("password123");

        when(userDetailsService.verifyUsernameUniqueness("newuser")).thenReturn(false);
        when(userDetailsService.verifyEmailUniqueness("newuser@example.com")).thenReturn(false);
        when(passwordHasher.hashPassword("password123")).thenReturn("hashedPassword");

        UserDetails userDetails = new UserDetails();
        UUID userId = UUID.randomUUID();
        userDetails.setId(userId);

        when(userDetailsService.addUserDetails(any(AddUserRequestDto.class))).thenReturn(userDetails);
        when(userService.addUser(any(AddUserRequestDto.class), eq(userId))).thenReturn(new UserProfile());

        // Act
        authService.register(request);

        // Assert
        verify(userDetailsService, times(1)).addUserDetails(any(AddUserRequestDto.class));
        verify(userService, times(1)).addUser(any(AddUserRequestDto.class), eq(userId));
    }

    @Test
    void register_usernameAlreadyExists_throwsBadRequestExceptionWithCorrectMessage() {

        AddUserRequestDto request = new AddUserRequestDto();
        request.setUsername("existinguser");
        request.setEmail("newemail@example.com");
        request.setPassword("password123");

        when(userDetailsService.verifyUsernameUniqueness("existinguser")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.register(request));

        verify(userDetailsService, times(1)).verifyUsernameUniqueness("existinguser");
        verify(userDetailsService, never()).verifyEmailUniqueness(anyString());
        verify(userDetailsService, never()).addUserDetails(any());
        verify(userService, never()).addUser(any(), any());

        assert(exception.getMessage().equals("Username already exist"));
    }

    @Test
    void register_emailAlreadyExists_throwsBadRequestExceptionWithCorrectMessage() {
        AddUserRequestDto request = new AddUserRequestDto();
        request.setUsername("newuser");
        request.setEmail("existingemail@example.com");
        request.setPassword("password123");

        when(userDetailsService.verifyUsernameUniqueness("newuser")).thenReturn(false);
        when(userDetailsService.verifyEmailUniqueness("existingemail@example.com")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.register(request));

        verify(userDetailsService, times(1)).verifyUsernameUniqueness("newuser");
        verify(userDetailsService, times(1)).verifyEmailUniqueness("existingemail@example.com");
        verify(userDetailsService, never()).addUserDetails(any());
        verify(userService, never()).addUser(any(), any());

        assert(exception.getMessage().equals("Email already exist"));
    }

}
