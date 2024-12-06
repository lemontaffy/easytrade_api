package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.mapper.UserMapper;
import com.cake.easytrade.model.LoginDTO;
import com.cake.easytrade.model.User;
import com.cake.easytrade.service.AuthService;
import com.cake.easytrade.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


}
