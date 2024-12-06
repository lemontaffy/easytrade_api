package com.cake.easytrade.config;

import io.jsonwebtoken.security.Keys;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.SecretKey;

public class JwtKeyProvider {
    private static final String ENV_SECRET_KEY = "JWT_SECRET_KEY";

    public static SecretKey getSecretKey() {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

        // Fetch the secret key
        String secret = dotenv.get(ENV_SECRET_KEY);

        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("Invalid or missing JWT_SECRET_KEY environment variable");
        }

        // Convert the secret to a SecretKey
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
