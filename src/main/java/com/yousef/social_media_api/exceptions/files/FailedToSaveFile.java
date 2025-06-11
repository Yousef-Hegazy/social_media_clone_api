package com.yousef.social_media_api.exceptions.files;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class FailedToSaveFile extends RuntimeException {
    public FailedToSaveFile(String message) {
        super(message);
    }
}
