package com.yousef.social_media_api.controllers;


import com.yousef.social_media_api.dtos.auth.CurrentUserResponse;
import com.yousef.social_media_api.dtos.auth.LoginRequest;
import com.yousef.social_media_api.dtos.auth.LoginResponse;
import com.yousef.social_media_api.dtos.auth.RegisterRequest;
import com.yousef.social_media_api.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/current-user")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(Authentication auth) {
        return ResponseEntity.ok(authService.getCurrentUser(auth));
    }
}
