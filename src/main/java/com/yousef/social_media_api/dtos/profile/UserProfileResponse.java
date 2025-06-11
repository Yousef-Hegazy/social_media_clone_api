package com.yousef.social_media_api.dtos.profile;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserProfileResponse(
        UUID id,
        String name,
        String email,
        String bio,
        String imageUrl
) {
}
