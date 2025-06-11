package com.yousef.social_media_api.dtos.auth;

public record UpdateUserRequest(
     String name,
     String email,
     String bio
) {
}
