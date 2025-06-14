package com.yousef.social_media_api.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotNull
        @NotEmpty
        @NotBlank
        String text
) {
}
