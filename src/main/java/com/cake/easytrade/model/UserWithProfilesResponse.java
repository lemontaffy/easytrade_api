package com.cake.easytrade.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithProfilesResponse {
    private User user;                      // The user information
    private List<UserProfileMulti> profiles; // List of user profiles
}