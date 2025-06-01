package com.yousef.social_media_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SocialMediaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApiApplication.class, args);
	}

}
