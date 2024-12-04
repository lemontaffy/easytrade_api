package com.cake.easytrade.controller;

import com.cake.easytrade.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/user")
    public Object getUserInfo(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return authService.authenticate(email, password);
    }
}
