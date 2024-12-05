package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.model.User;
import com.cake.easytrade.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "UserController", description = "userController")
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Const.API)
public class UserController extends BaseController {

    private static final String path = "/user";

    private final UserService userService;

    @Operation(summary = "개인 사용자 조회")
    @GetMapping(path = path+"/{id}", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws Exception {
        Optional<User> user = userService.getUserById(id);
        return user.map(this::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PostMapping(path = path+"/register", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        User createdUser = userService.createUser(user);
        return created(createdUser);
    }

    @PutMapping(path = path+"/{id}", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ok(updatedUser);
    }

    @DeleteMapping(path = path+"/{id}", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return noContent();
    }
}