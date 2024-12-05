package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AuthController", description = "authController")
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Const.API)
public class AuthController extends BaseController {

    private static final String path = "/auth";

    private final AuthService authService;

    @GetMapping(path = path+"/{user}", produces = Const.REST_CONTENT_TYPE)
    public Object getUserInfo(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @PostMapping(path = path+"/{login}", produces = Const.REST_CONTENT_TYPE)
    public String login(@RequestParam String email, @RequestParam String password) throws Exception {
        return authService.authenticate(email, password);
    }
}
