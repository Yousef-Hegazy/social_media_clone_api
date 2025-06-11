package com.yousef.social_media_api.exceptions.profile;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProfileException extends RuntimeException {
    private String message;

    @Override
    public String getMessage() {return message;}
}
