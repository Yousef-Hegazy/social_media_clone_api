package com.yousef.social_media_api.dtos.auth;

import lombok.Builder;

import java.util.UUID;

@Builder
public record LoginResponse(
        UUID id,
        String email,
        String name,
        String token
) {
}
