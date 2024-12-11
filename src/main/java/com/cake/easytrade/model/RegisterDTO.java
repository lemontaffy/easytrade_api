package com.cake.easytrade.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private User user;
    private String photoUrl;
}
