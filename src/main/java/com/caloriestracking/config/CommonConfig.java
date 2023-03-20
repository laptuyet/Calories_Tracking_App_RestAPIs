package com.caloriestracking.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CommonConfig {
	
	private final Environment env;

	@Bean
	Cloudinary cloudinary() {
		
		Map<String, String> cloudConfigs = new HashMap<>();
		cloudConfigs.put("cloud_name", env.getProperty("cloudinary.name"));
		cloudConfigs.put("api_key", env.getProperty("cloudinary.api-key"));
		cloudConfigs.put("api_secret", env.getProperty("cloudinary.api-secret"));
		
		Cloudinary cloudinary = new Cloudinary(cloudConfigs);
		return cloudinary;
	}
}
