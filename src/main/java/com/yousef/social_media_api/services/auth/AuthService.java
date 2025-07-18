package com.yousef.social_media_api.services.auth;

import com.yousef.social_media_api.dtos.auth.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse getCurrentUser(Authentication auth);

    LoginResponse updateUser(UpdateUserRequest user, MultipartFile image, Authentication auth);
}
