package com.yousef.social_media_api.exceptions.files;

public class MaximumFilesReached extends RuntimeException {
    public MaximumFilesReached(String message) {
        super(message);
    }
}
