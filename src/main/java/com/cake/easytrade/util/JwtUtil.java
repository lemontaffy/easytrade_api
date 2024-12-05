package com.cake.easytrade.util;

import com.cake.easytrade.config.JwtKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // Define token expiration time (1 day in milliseconds)
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    // Secret key generation
    private static final SecretKey SECRET_KEY = JwtKeyProvider.getSecretKey();


    /**
     * Generate a JWT token for a user.
     *
     * @param email The email of the user.
     * @param role  The role of the user.
     * @return A JWT token string.
     */
    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // Add custom claims as needed
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate and parse a JWT token.
     *
     * @param token The JWT token.
     * @return Claims object containing the token's payload.
     * @throws Exception If the token is invalid or expired.
     */
    public static Claims validateToken(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Use SecretKey for validation
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract the email from a token.
     *
     * @param token The JWT token.
     * @return The email from the token's payload.
     */
    public static String extractEmail(String token) throws Exception {
        return validateToken(token).getSubject();
    }

    /**
     * Extract the role from a token.
     *
     * @param token The JWT token.
     * @return The role from the token's payload.
     */
    public static String extractRole(String token) throws Exception {
        return (String) validateToken(token).get("role");
    }
}

