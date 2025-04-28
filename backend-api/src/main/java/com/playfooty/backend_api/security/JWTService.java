package com.playfooty.backend_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.playfooty.backendCore.exception.ExpiredJWTException;
import com.playfooty.backendCore.exception.UnauthorizedException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class JWTService {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}")
    private int expiryInSeconds;

    private Algorithm algorithm;

    private static final String USERID = "userId";

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
        LOGGER.info("JWTService initialized with issuer: {} and expiry: {} seconds", issuer, expiryInSeconds);
    }

    public String generate(HashMap<String, String> claims) {
        LOGGER.debug("Generating JWT with default expiry: {} seconds", expiryInSeconds);
        return generate(claims, expiryInSeconds);
    }

    public String generate(HashMap<String, String> claims, int customExpiryInSeconds) {
        LOGGER.debug("Generating JWT with custom expiry: {} seconds", customExpiryInSeconds);
        JWTCreator.Builder jwt = JWT.create();

        claims.forEach((key, value) -> {
            LOGGER.trace("Adding claim: {} = {}", key, value);
            jwt.withClaim(key, value);
        });

        Date expiryDate = new Date(System.currentTimeMillis() + (1000L * customExpiryInSeconds));
        jwt.withExpiresAt(expiryDate);
        jwt.withIssuer(issuer);

        String token = jwt.sign(algorithm);
        LOGGER.info("JWT generated with expiry: {}", expiryDate);
        return token;
    }

    public DecodedJWT decode(String token) throws RuntimeException {
        LOGGER.debug("Decoding JWT");

        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            LOGGER.info("JWT successfully decoded for issuer: {}", decodedJWT.getIssuer());
            return decodedJWT;

        }
        catch (TokenExpiredException e) {
            LOGGER.error("Failed to decode JWT: {}", e.getMessage());
            throw new ExpiredJWTException();
        }
        catch (JWTVerificationException e) {
            LOGGER.error("Failed to decode JWT: {}", e.getMessage());
            throw new UnauthorizedException();
        } catch (RuntimeException e) {
            LOGGER.error("Failed to decode JWT: {}", e.getMessage());
            throw new RuntimeException("JWT decoding failed", e);
        }
    }

    public Claim getClaim(String token, String claim) {
        LOGGER.debug("Extracting claim '{}' from token", claim);
        DecodedJWT decodedJWT = decode(token);
        Claim result = decodedJWT.getClaim(claim);

        if (result.isNull()) {
            LOGGER.warn("Claim '{}' not found in token", claim);
        } else {
            LOGGER.info("Claim '{}' extracted with value: {}", claim, result.asString());
        }

        return result;
    }
}
