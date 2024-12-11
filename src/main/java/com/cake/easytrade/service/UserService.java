package com.cake.easytrade.service;

import com.cake.easytrade.mapper.PermissionMapper;
import com.cake.easytrade.mapper.RoleMapper;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.User;
import com.cake.easytrade.model.UserProfileMulti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<User> getUserById(Long id) {
        return userMapper.findById(id);
    }

    @Transactional
    public User registerUser(User user, String photoUrl) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Insert user into users table
        userMapper.insertUser(user);
        Long userId = user.getId();

        permissionMapper.insertUserRole(userId, user.getRoleId());

        // Create default profile (nickname must already be in user object)
        UserProfileMulti profile = new UserProfileMulti();
        profile.setUserId(userId);
        profile.setPhotoUrl(photoUrl);
        profile.setNickname(user.getNickname()); // Provided from frontend
        profile.setActive(true);
        userMapper.insertProfile(profile);

        // Set active profile in users table
        userMapper.setActiveProfile(profile.getId());

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

    public List<UserProfileMulti> getUserProfiles(Long userId) {
        return userMapper.getUserProfiles(userId);
    }

    @Transactional
    public void setActiveProfile(Long userId, Long profileId) {
        userMapper.deactivateProfiles(userId);
        userMapper.setActiveProfile(profileId);
    }
}
