package com.cake.easytrade.service;

import com.cake.easytrade.mapper.PermissionMapper;
import com.cake.easytrade.mapper.RoleMapper;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<User> getUserById(Long id) throws Exception {
        return Optional.ofNullable(userMapper.findById(id));
    }

    public User createUser(User user) throws Exception {
        // Encrypt the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Insert user into the database
        userMapper.insertUser(user);
        permissionMapper.insertUserRole(user.getId(), 2L);

        return user;
    }

    public void deleteUser(Long id) {
        userMapper.deleteUser(id);
    }

    public User updateUser(User user) {
        if ("local".equals(user.getAuthProvider())) {
            userMapper.updateLocalUser(user);
        } else {
            userMapper.updateSsoUser(user);
        }

        return user;
    }
}
