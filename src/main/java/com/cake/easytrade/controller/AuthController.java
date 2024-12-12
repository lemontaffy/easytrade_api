package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.CustomUserDetails;
import com.cake.easytrade.model.LoginDTO;
import com.cake.easytrade.model.User;
import com.cake.easytrade.service.AuthService;
import com.cake.easytrade.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "AuthController", description = "authController")
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Const.API)
public class AuthController extends BaseController {

    private static final String path = "/auth";

    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping(path = path+"/login", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception {
        try {
            // Authenticate and generate tokens
            Map<String, String> tokens = authService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

            // Return tokens in the response
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException ex) {
            log.warn("Login failed: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping(path = path+"/refresh", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            // Call the service method to refresh the token
            String newAccessToken = authService.refreshToken(refreshToken);

            // Return the new access token
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping(path = path+"/protected", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> protectedResource() {
        return ResponseEntity.ok(Map.of("message", "Authenticated"));
    }

    @GetMapping(path = path+"/status", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication status: {}", authentication);

        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            // If authenticated
            response.put("loggedIn", true);

            // Assuming your user object has a method to get the profile photo
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            response.put("profilePhoto", userMapper.getActiveProfile(userDetails.getUserId(), true).getPhotoUrl()); // Replace with your actual profile photo method
            response.put("userId", userDetails.getUserId());
            response.put("activeProfileId", userMapper.getActiveProfile(userDetails.getUserId(), true).getId());
        } else {
            // Not authenticated
            response.put("loggedIn", false);
            response.put("profilePhoto", null);
            response.put("userId", null);
            response.put("activeProfileId", null);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = path+"/logout", produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the refresh token if stored in a database or cache
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            invalidateRefreshToken(refreshToken); // Implement this method to handle token invalidation
        }

//        // Clear access token cookies if applicable
//        Cookie accessTokenCookie = new Cookie("accessToken", null);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(true); // Use only in production
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(0); // Expire immediately
//        response.addCookie(accessTokenCookie);
//
//        // Clear refresh token cookies if applicable
//        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true); // Use only in production
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(0); // Expire immediately
//        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    private void invalidateRefreshToken(String refreshToken) {
        // Logic to invalidate the refresh token
        // e.g., add the token to a blacklist or remove it from the database/cache
        System.out.println("Invalidating refresh token: " + refreshToken);
    }

}
