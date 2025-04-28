package com.playfooty.backend_api.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.playfooty.backendCore.exception.ExpiredJWTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();

        // Inject properties manually
        ReflectionTestUtils.setField(jwtService, "algorithmKey", "test-key-123");
        ReflectionTestUtils.setField(jwtService, "issuer", "playfooty-test");
        ReflectionTestUtils.setField(jwtService, "expiryInSeconds", 2); // short expiry for testing

        jwtService.postConstruct();
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        HashMap<String, String> claims = new HashMap<>();
        claims.put("userId", "abc123");

        String token = jwtService.generate(claims);
        assertNotNull(token);

        DecodedJWT decodedJWT = jwtService.decode(token);
        assertEquals("playfooty-test", decodedJWT.getIssuer());
        assertEquals("abc123", decodedJWT.getClaim("userId").asString());
    }

    @Test
    void decode_shouldThrowExpiredJWTException_forExpiredToken() throws InterruptedException {
        HashMap<String, String> claims = new HashMap<>();
        claims.put("userId", "abc123");

        String token = jwtService.generate(claims, 1);
        Thread.sleep(3000); // wait for token to expire

        assertThrows(ExpiredJWTException.class, () -> jwtService.decode(token));
    }

    @Test
    void getClaim_shouldReturnCorrectClaim() {
        HashMap<String, String> claims = new HashMap<>();
        claims.put("role", "admin");

        String token = jwtService.generate(claims);
        Claim claim = jwtService.getClaim(token, "role");

        assertEquals("admin", claim.asString());
    }
}
