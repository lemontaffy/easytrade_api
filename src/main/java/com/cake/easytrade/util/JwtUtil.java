package com.cake.easytrade.util;

import com.cake.easytrade.config.JwtKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Expiration times
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    // Separate secret keys
    private static final SecretKey ACCESS_SECRET_KEY = JwtKeyProvider.getSecretKey(); // Secret for Access Token
    private static final SecretKey REFRESH_SECRET_KEY = JwtKeyProvider.getSecretKey(); // Secret for Refresh Token

    /**
     * Generate an Access Token for a user.
     *
     * @param email The email of the user.
     * @param role  The role of the user.
     * @return A JWT Access Token.
     */
    public static String generateAccessToken(String email, String role, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(ACCESS_SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generate a Refresh Token for a user.
     *
     * @param email The email of the user.
     * @return A JWT Refresh Token.
     */
    public static String generateRefreshToken(String email, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(REFRESH_SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate an Access Token.
     *
     * @param token The JWT Access Token.
     * @return Claims object containing the token's payload.
     * @throws Exception If the token is invalid or expired.
     */
    public static Claims validateAccessToken(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validate a Refresh Token.
     *
     * @param token The JWT Refresh Token.
     * @return Claims object containing the token's payload.
     * @throws Exception If the token is invalid or expired.
     */
    public static Claims validateRefreshToken(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(REFRESH_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract the email from a token.
     *
     * @param token The JWT token.
     * @param isAccessToken True if token is Access Token, false if Refresh Token.
     * @return The email from the token's payload.
     * @throws Exception If the token is invalid or expired.
     */
    public static String extractEmail(String token, boolean isAccessToken) throws Exception {
        Claims claims = isAccessToken ? validateAccessToken(token) : validateRefreshToken(token);
        return claims.getSubject();
    }

    /**
     * Extract the role from an Access Token.
     *
     * @param token The JWT Access Token.
     * @return The role from the token's payload.
     * @throws Exception If the token is invalid or expired.
     */
    public static String extractRole(String token) throws Exception {
        return (String) validateAccessToken(token).get("role");
    }
}
