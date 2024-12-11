package com.cake.easytrade.service;

import com.cake.easytrade.mapper.PermissionMapper;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.CustomUserDetails;
import com.cake.easytrade.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by username for local login
        User user = null;
        try {
            user = userMapper.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return buildUserDetails(user);
    }

    public UserDetails loadUserById(Long userId) {
        // Fetch user by ID
        User user = userMapper.findById(userId).orElseThrow(() ->
                new UsernameNotFoundException("User not found with ID: " + userId));

        // Fetch role for the user
        Long roleId = permissionMapper.findRoleIdByUserId(user.getId());
        String role = (roleId != null && roleId == 1) ? "ROLE_ADMIN" : "ROLE_USER";

        // Build and return CustomUserDetails
        return new CustomUserDetails(
                user.getId().intValue(),  // Convert userId to Integer
                user.getUsername(),
                user.getPassword(),      // Ensure password is hashed
                Collections.singletonList(new SimpleGrantedAuthority(role)) // Assign role
        );
    }

//    public UserDetails loadUserByLinkedAuth(String linkedAuthId, String authProvider) {
//        // Fetch user by linkedAuthId and authProvider for SSO login
//        User user = userMapper.findByLinkedAuth(linkedAuthId, authProvider);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("SSO user not found with ID: " + linkedAuthId);
//        }
//
//        return buildUserDetails(user);
//    }

    private UserDetails buildUserDetails(User user) {
        Long roleId = permissionMapper.findRoleIdByUserId(user.getId()); // Call the mapper to get roleId

        // Null-safe role assignment
        String role = (roleId != null && roleId == 1) ? "ROLE_ADMIN" : "ROLE_USER";

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Ensure password is properly hashed
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
