package com.cake.easytrade.service;

import com.cake.easytrade.mapper.PermissionMapper;
import com.cake.easytrade.mapper.RoleMapper;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.Permission;
import com.cake.easytrade.model.Role;
import com.cake.easytrade.model.User;

import com.cake.easytrade.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public String authenticate(String email, String password) throws Exception {
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
        if (permission == null) {
            throw new RuntimeException("User does not have a role assigned");
        }

        // Step 4: Find role using the permission's roleId
        Role role = roleMapper.findById(permission.getRoleId());
        if (role == null) {
            throw new RuntimeException("Invalid role assigned to user");
        }

        // Generate JWT token for authenticated user
        return JwtUtil.generateToken(user.getEmail(), role.getName());
    }
}