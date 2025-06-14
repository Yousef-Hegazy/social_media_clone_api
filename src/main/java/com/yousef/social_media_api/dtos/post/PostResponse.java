package com.yousef.social_media_api.dtos.post;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record PostResponse(
        UUID id,
        UUID userId,
        String username,
        String text,
        String imageUrl,
        Date timestamp
) {
}
