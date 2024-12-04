package com.cake.easytrade.service;

import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserMapper userMapper;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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
}