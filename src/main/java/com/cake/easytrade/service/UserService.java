package com.cake.easytrade.service;



import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public Optional<User> getUserById(Long id) throws Exception {
        return Optional.ofNullable(userMapper.findById(id));
    }

    public User createUser(User user) throws Exception {
        userMapper.insertUser(user);
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
