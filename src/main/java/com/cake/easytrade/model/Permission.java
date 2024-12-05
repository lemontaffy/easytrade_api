package com.cake.easytrade.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private String userId;
    private String roleId;
}
