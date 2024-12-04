package com.cake.easytrade.controller;

import com.cake.easytrade.model.User;
import com.cake.easytrade.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws Exception {
        Optional<User> user = userService.getUserById(id);
        return user.map(this::ok).orElseGet(() -> notFound("User not found"));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        User createdUser = userService.createUser(user);
        return created(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return noContent();
    }
}