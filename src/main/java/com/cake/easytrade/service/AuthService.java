package com.cake.easytrade.service;

import com.cake.easytrade.mapper.PermissionMapper;
import com.cake.easytrade.mapper.RoleMapper;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.Permission;
import com.cake.easytrade.model.Role;
import com.cake.easytrade.model.User;

import com.cake.easytrade.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<User> findOrCreateUser(String email, String authProvider, String authId) throws Exception {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setAuthProvider(authProvider);
            user.setAuthId(authId);
            userMapper.insertUser(user);
        }
        return Optional.of(user);
    }

    public Map<String, String> authenticate(String email, String password) throws Exception {
        // Fetch user from the database
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // Compare hashed password with the plain text password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Step 3: Find permission for the user
        Permission permission = permissionMapper.findByUserId(user.getId());
        log.debug("Permission for user ID " + user.getId() + ": " + permission);
        if (permission == null) {
            throw new RuntimeException("User does not have a role assigned");
        }

        // Step 4: Find role using the permission's roleId
        Role role = roleMapper.findById(permission.getRoleId());
        if (role == null) {
            throw new RuntimeException("Invalid role assigned to user");
        }

        // Step 5: Generate Access Token and Refresh Token
        String accessToken = JwtUtil.generateAccessToken(user.getEmail(), role.getName(), user.getId());
        String refreshToken = JwtUtil.generateRefreshToken(user.getEmail(), user.getId());


        // Step 6: Return both tokens in a map
        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    public String refreshToken(String refreshToken) throws Exception {
        // Step 1: Validate the Refresh Token
        Claims claims = JwtUtil.validateRefreshToken(refreshToken);

        // Step 2: Extract the email from the Refresh Token
        String email = claims.getSubject();

        // Step 3: Ensure the user still exists
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Step 4: Retrieve the user's role
        Permission permission = permissionMapper.findByUserId(user.getId());
        if (permission == null) {
            throw new RuntimeException("User does not have any assigned roles");
        }

        Role role = roleMapper.findById(permission.getRoleId());
        if (role == null) {
            throw new RuntimeException("Invalid role assigned to user");
        }

        // Step 5: Generate a new Access Token
        return JwtUtil.generateAccessToken(email, role.getName(), user.getId());
    }
}