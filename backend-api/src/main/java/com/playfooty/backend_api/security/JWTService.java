package com.playfooty.backend_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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
    }

    public String generate (HashMap<String, String> claims) {
        JWTCreator.Builder jwt = JWT.create();

        claims.forEach((key, value) -> jwt.withClaim(key, value));
        jwt.withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)));
        jwt.withIssuer(issuer);

        return jwt.sign(algorithm);
    }

    public DecodedJWT decode (String token) throws RuntimeException {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        if (decodedJWT.getExpiresAt().before(new Date())) {
            throw new ExpiredJWTException();
        }

        return JWT.decode(token);
    }

    public Claim getClaim(String token, String claim) {
        DecodedJWT decodedJWT = decode(token);

        return decodedJWT.getClaim(claim);
    }
}
