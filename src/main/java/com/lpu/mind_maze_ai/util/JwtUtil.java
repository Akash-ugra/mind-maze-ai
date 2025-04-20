package com.lpu.mind_maze_ai.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Utility class for handling JSON Web Token (JWT) operations.
 * Provides functionality for:
 * - Token generation
 * - Token validation
 * - Claims extraction
 */
@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey key;
    private final long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    /**
     * Generates a JWT token for a user with specified claims.
     *
     * @param username User's username
     * @param userId   User's unique identifier
     * @param name     User's full name
     * @param roles    User's assigned roles
     * @return Generated JWT token string
     */
    public String generateToken(String username, Long userId, String name, List<String> roles) {
        Date now = new Date();
        try {
            return Jwts.builder()
                    .subject(username)
                    .claim("userId", userId)
                    .claim("name", name)
                    .claim("roles", roles)
                    .issuedAt(now)
                    .expiration(new Date(now.getTime() + expirationTime))
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token", e);
            throw new RuntimeException("Could not generate token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            return extractClaims(token).getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            throw new JwtException("Invalid token", e);
        }
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (Exception e) {
            logger.error("Error extracting claims from token", e);
            throw new JwtException("Invalid token", e);
        }
    }

    /**
     * Validates a JWT token.
     *
     * @param token Token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
