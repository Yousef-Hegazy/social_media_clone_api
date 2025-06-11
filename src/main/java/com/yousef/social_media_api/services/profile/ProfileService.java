package com.yousef.social_media_api.services.profile;

import com.yousef.social_media_api.dtos.profile.UserProfileResponse;

import java.util.UUID;

public interface ProfileService {
    UserProfileResponse getProfile(UUID id);
}
