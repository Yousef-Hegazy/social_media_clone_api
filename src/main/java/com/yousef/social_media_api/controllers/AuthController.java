package com.yousef.social_media_api.controllers;


import com.yousef.social_media_api.dtos.auth.*;
import com.yousef.social_media_api.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/update-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CurrentUserResponse> updateUser(
            @RequestPart("user") @Valid UpdateUserRequest user,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication auth
            ) {
        return ResponseEntity.ok(authService.updateUser(user, image, auth));
    }
}
