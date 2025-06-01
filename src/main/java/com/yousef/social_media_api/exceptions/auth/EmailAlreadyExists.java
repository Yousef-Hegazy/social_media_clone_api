package com.yousef.social_media_api.exceptions.auth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EmailAlreadyExists extends RuntimeException {
    private String message;

    @Override
    public String getMessage() {return message;}
}
