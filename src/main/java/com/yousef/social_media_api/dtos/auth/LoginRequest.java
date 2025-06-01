package com.yousef.social_media_api.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank
        @NotEmpty
        @NotNull
        String email,

        @NotBlank
        @NotEmpty
        @NotNull
        String password
) {
}
