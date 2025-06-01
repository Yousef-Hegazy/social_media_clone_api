package com.yousef.social_media_api.services.auth;

import com.yousef.social_media_api.dtos.auth.CurrentUserResponse;
import com.yousef.social_media_api.dtos.auth.LoginRequest;
import com.yousef.social_media_api.dtos.auth.LoginResponse;
import com.yousef.social_media_api.dtos.auth.RegisterRequest;
import com.yousef.social_media_api.exceptions.auth.EmailAlreadyExists;
import com.yousef.social_media_api.models.auth.AppUser;
import com.yousef.social_media_api.repositories.auth.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse register(RegisterRequest request) {
        final boolean exists = userRepository.existsByEmail(request.email());

        if (exists) {
            throw new EmailAlreadyExists("User with this email already exists: " + request.email());
        }

        final AppUser savedUser = userRepository.save(AppUser.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .build());

        return LoginResponse.builder()
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .name(savedUser.getName())
                .token(jwtService.generateToken(savedUser))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        if (!auth.isAuthenticated()) {
            throw new BadCredentialsException("Wrong credentials, please make sure you entered the email and password correctly");
        }

        final AppUser user = (AppUser) auth.getPrincipal();

        return LoginResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .token(jwtService.generateToken(user))
                .build();
    }

    @Override
    public CurrentUserResponse getCurrentUser(Authentication auth) {
        final AppUser user = (AppUser) auth.getPrincipal();

        if (user == null) {
            throw new UsernameNotFoundException("No user is authenticated");
        }

        return CurrentUserResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .token(jwtService.generateToken(user))
                .build();
    }
}
