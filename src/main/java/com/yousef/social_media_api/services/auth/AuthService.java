package com.yousef.social_media_api.services.auth;

import com.yousef.social_media_api.dtos.auth.CurrentUserResponse;
import com.yousef.social_media_api.dtos.auth.LoginRequest;
import com.yousef.social_media_api.dtos.auth.LoginResponse;
import com.yousef.social_media_api.dtos.auth.RegisterRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    CurrentUserResponse getCurrentUser(Authentication auth);
}
