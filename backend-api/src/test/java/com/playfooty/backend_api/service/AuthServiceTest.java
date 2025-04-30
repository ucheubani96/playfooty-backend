package com.playfooty.backend_api.service;

import com.playfooty.backendCore.dto.AddUserRequestDto;
import com.playfooty.backendCore.exception.BadRequestException;
import com.playfooty.backendCore.exception.UnauthorizedException;
import com.playfooty.backend_api.dto.LoginRequestDTO;
import com.playfooty.backend_api.dto.LoginResponseDTO;
import com.playfooty.backend_api.model.UserDetails;
import com.playfooty.backend_api.security.JWTService;
import com.playfooty.userManagement.model.UserProfile;
import com.playfooty.userManagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthService authService;

    //
    // register(...) tests (unchanged except for minor logging)
    //

    @Test
    void register_successfulRegistration() {
        AddUserRequestDto req = new AddUserRequestDto();
        req.setUsername("newuser");
        req.setEmail("new@example.com");
        req.setPassword("pwd");

        when(userDetailsService.verifyUsernameUniqueness("newuser")).thenReturn(false);
        when(userDetailsService.verifyEmailUniqueness("new@example.com")).thenReturn(false);
        when(passwordHasher.hashPassword("pwd")).thenReturn("hashed");
        UserDetails ud = new UserDetails(); ud.setId(UUID.randomUUID());
        when(userDetailsService.addUserDetails(any())).thenReturn(ud);
        when(userService.addUser(any(), eq(ud.getId()))).thenReturn(new UserProfile());

        authService.register(req);

        verify(userDetailsService).addUserDetails(any());
        verify(userService).addUser(any(), eq(ud.getId()));
    }

    @Test
    void register_usernameExists_throwsBadRequest() {
        AddUserRequestDto req = new AddUserRequestDto();
        req.setUsername("exists");
        req.setEmail("e@e.com");
        req.setPassword("pwd");

        when(userDetailsService.verifyUsernameUniqueness("exists")).thenReturn(true);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> authService.register(req)
        );
        assertEquals("Username already exist", ex.getMessage());

        verify(userDetailsService).verifyUsernameUniqueness("exists");
        verifyNoMoreInteractions(userDetailsService, userService);
    }

    @Test
    void register_emailExists_throwsBadRequest() {
        AddUserRequestDto req = new AddUserRequestDto();
        req.setUsername("u");
        req.setEmail("exists@e.com");
        req.setPassword("pwd");

        when(userDetailsService.verifyUsernameUniqueness("u")).thenReturn(false);
        when(userDetailsService.verifyEmailUniqueness("exists@e.com")).thenReturn(true);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> authService.register(req)
        );
        assertEquals("Email already exist", ex.getMessage());

        verify(userDetailsService).verifyUsernameUniqueness("u");
        verify(userDetailsService).verifyEmailUniqueness("exists@e.com");
        verify(userDetailsService, never()).addUserDetails(any());
        verify(userService, never()).addUser(any(), any());
    }

    //
    // login(...) tests
    //

    @Test
    void login_successful_returnsToken() {
        // Arrange
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("joe@example.com");
        req.setPassword("plainpwd");

        UserDetails ud = new UserDetails();
        ud.setId(UUID.randomUUID());
        ud.setPassword("hashedpwd");
        ud.setIsActive(true);

        when(userDetailsService.findByEmail("joe@example.com"))
                .thenReturn(Optional.of(ud));
        when(passwordHasher.matches("plainpwd", "hashedpwd"))
                .thenReturn(true);
        when(jwtService.generate(any(HashMap.class)))
                .thenReturn("jwt-token-123");


        // Act
        LoginResponseDTO resp = authService.login(req);

        // Assert
        assertNotNull(resp);
        assertEquals("jwt-token-123", resp.getToken());

        verify(userDetailsService).findByEmail("joe@example.com");
        verify(passwordHasher).matches("plainpwd", "hashedpwd");
        verify(jwtService).generate(argThat(arg ->
                arg instanceof HashMap &&
                arg.containsKey("id") &&
                arg.get("id").equals(ud.getId().toString())
        ));
    }

    @Test
    void login_nonexistentEmail_throwsBadCredentials() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("noone@nowhere.com");
        req.setPassword("any");

        when(userDetailsService.findByEmail("noone@nowhere.com"))
                .thenReturn(Optional.empty());

        BadCredentialsException ex = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(req)
        );
        assertEquals("Invalid credentials", ex.getMessage());

        verify(userDetailsService).findByEmail("noone@nowhere.com");
        verifyNoMoreInteractions(passwordHasher, jwtService);
    }

    @Test
    void login_wrongPassword_throwsBadCredentials() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("joe@example.com");
        req.setPassword("wrongpwd");

        UserDetails ud = new UserDetails();
        ud.setPassword("realhashed");
        ud.setIsActive(true);

        when(userDetailsService.findByEmail("joe@example.com"))
                .thenReturn(Optional.of(ud));
        when(passwordHasher.matches("wrongpwd", "realhashed"))
                .thenReturn(false);

        BadCredentialsException ex = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(req)
        );
        assertEquals("Invalid credentials", ex.getMessage());

        verify(userDetailsService).findByEmail("joe@example.com");
        verify(passwordHasher).matches("wrongpwd", "realhashed");
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void login_inactiveUser_throwsUnauthorized() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("joe@example.com");
        req.setPassword("anypwd");

        UserDetails ud = new UserDetails();
        ud.setPassword("ignored");
        ud.setIsActive(false);

        when(userDetailsService.findByEmail("joe@example.com"))
                .thenReturn(Optional.of(ud));
        when(passwordHasher.matches(anyString(), anyString()))
                .thenReturn(true);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> authService.login(req)
        );
        assertEquals("Account inactive", ex.getMessage());

        verify(userDetailsService).findByEmail("joe@example.com");
        verify(passwordHasher).matches(anyString(), anyString());
        verifyNoMoreInteractions(jwtService);
    }
}
