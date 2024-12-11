package com.cake.easytrade.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileMulti {
    private Long id;
    private Long userId;
    private String photoUrl;
    private String nickname;
    private LocalDateTime createdAt;
    private boolean isActive;
}