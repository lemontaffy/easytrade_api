package com.cake.easytrade.model;

import com.cake.easytrade.handler.CommaSeparatedStringTypeHandler;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.TypeHandler;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    @Size(max = 50)
    private String username;

    @Email
    private String email;
    private String password; // Nullable for SSO

    @NotBlank
    private String authProvider; // 'local', 'google', etc.
    private String authId; // Unique identifier from SSO

    @Builder.Default
    private List<String> linkedAuthId = new ArrayList<>(); // Initialize to an empty list
    private LocalDateTime createdAt;

    @Size(max = 50)
    private String nickname;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 50)
    private String account;

    @Size(max = 50)
    private String personalCustomCode;
    private LocalDateTime updatedAt;

    public boolean enabled;
}
