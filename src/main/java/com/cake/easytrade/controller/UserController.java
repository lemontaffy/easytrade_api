package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.model.*;
import com.cake.easytrade.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping(path = path, produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        log.debug("Auth Details: {}", authentication);

        // Check if authentication is null
        if (authentication == null) {
            log.warn("No authentication provided.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication is required.");
        }

        // Check if authentication principal is an instance of CustomUserDetails
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Integer userIdClaim = userDetails.getUserId(); // Fetch userId from CustomUserDetails
            log.info("Fetching profile for user ID: {}", userIdClaim);

            Long userId = userIdClaim.longValue(); // Convert Integer to Long

            // Fetch user from the database
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                log.warn("User not found for ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            // Fetch user profiles
            List<UserProfileMulti> profiles = userService.getUserProfiles(userId);
            log.debug("User profiles fetched: {}", profiles);

            // Prepare response
            UserWithProfilesResponse response = new UserWithProfilesResponse(user.get(), profiles);
            log.info("Returning user profile response: {}", response);

            return ResponseEntity.ok(response);
        }

        // Handle unsupported principal type
        log.warn("Authentication principal is not of type CustomUserDetails.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication principal.");
    }

    @PostMapping(path = path+"/register", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> createUser(@RequestBody RegisterDTO request) throws Exception {
        User createdUser = userService.registerUser(request.getUser(), request.getPhotoUrl());
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