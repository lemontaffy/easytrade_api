package com.cake.easytrade.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password; // Nullable for SSO
    private String authProvider; // 'local', 'google', etc.
    private String authId; // Unique identifier from SSO
    private LocalDateTime createdAt;

    // Getters and setters
}
