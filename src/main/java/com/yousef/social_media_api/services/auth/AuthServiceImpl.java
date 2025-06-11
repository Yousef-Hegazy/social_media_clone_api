package com.yousef.social_media_api.services.auth;

import com.yousef.social_media_api.dtos.auth.*;
import com.yousef.social_media_api.exceptions.auth.EmailAlreadyExists;
import com.yousef.social_media_api.models.auth.AppUser;
import com.yousef.social_media_api.models.profile.UserProfile;
import com.yousef.social_media_api.repositories.auth.AppUserRepository;
import com.yousef.social_media_api.services.files.AppFileType;
import com.yousef.social_media_api.services.files.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final FilesService filesService;

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
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .bio(savedUser.getProfile().getBio())
                .imageUrl(savedUser.getProfile().getImageUrl())
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
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getProfile().getBio())
                .imageUrl(user.getProfile().getImageUrl())
                .token(jwtService.generateToken(user))
                .build();
    }

    @Override
    public CurrentUserResponse getCurrentUser(Authentication auth) {
        final AppUser user = (AppUser) auth.getPrincipal();

        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("No user is authenticated");
        }

        return CurrentUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getProfile().getBio())
                .imageUrl(user.getProfile().getImageUrl())
                .token(jwtService.generateToken(user))
                .build();
    }

    @Override
    public CurrentUserResponse updateUser(UpdateUserRequest user, MultipartFile image, Authentication auth) {
        final AppUser authUser = (AppUser) auth.getPrincipal();

        if (authUser == null) {
            throw new AuthenticationCredentialsNotFoundException("The user is not logged in");
        }


        final AppUser dbUser = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("No such user"));

        if (!user.email().isEmpty()) dbUser.setEmail(user.email());

        if (!user.name().isEmpty()) dbUser.setName(user.name());

        if (dbUser.getProfile() != null) {
            dbUser.getProfile().setBio(user.bio());
        } else {
            dbUser.setProfile(UserProfile.builder()
                    .user(dbUser)
                    .bio(user.bio())
                    .build()
            );
        }

        final String imageUrl = filesService.saveFile(image, AppFileType.ProfileImage, dbUser.getId().toString());

        dbUser.getProfile().setImageUrl(imageUrl);

        final AppUser updatedUser = userRepository.save(dbUser);

        return CurrentUserResponse.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .bio(updatedUser.getProfile().getBio())
                .imageUrl(updatedUser.getProfile().getImageUrl())
                .token(jwtService.generateToken(updatedUser))
                .build();
    }
}
