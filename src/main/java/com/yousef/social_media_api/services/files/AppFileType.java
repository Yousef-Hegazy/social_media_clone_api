package com.yousef.social_media_api.services.files;

import lombok.Getter;

@Getter
public enum AppFileType {
    ProfileImage("profile_images"),
    PostsImages("posts_images");

    private final String name;

    AppFileType(String name) {
        this.name = name;
    }
}
