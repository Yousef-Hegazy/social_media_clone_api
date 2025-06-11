package com.yousef.social_media_api.services.profile;

import com.yousef.social_media_api.dtos.profile.UserProfileResponse;
import com.yousef.social_media_api.exceptions.profile.ProfileException;
import com.yousef.social_media_api.models.auth.AppUser;
import com.yousef.social_media_api.repositories.auth.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final AppUserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(UUID id) {
        final AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ProfileException("No profile was found for this id"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getProfile().getBio())
                .imageUrl(user.getProfile().getImageUrl())
                .build();
    }
}
