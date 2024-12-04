package com.cake.easytrade.service;



import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Optional<User> getUserById(Long id) throws Exception {
        return Optional.ofNullable(userMapper.findById(id));
    }

    public User createUser(User user) throws Exception {
        userMapper.insertUser(user);
        return user;
    }
}
