package com.example.itshop.configs;

import com.cloudinary.Cloudinary;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootConfiguration
@Log4j2
public class AppConfig {
	@Bean
	public Cloudinary cloudinary(
		@Value("${cloudinary.cloud-name}") String cloudName,
		@Value("${cloudinary.api-key}") String apiKey,
		@Value("${cloudinary.api-secret}") String apiSecret
	) {
		Map<String, String> config = new HashMap();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		return new Cloudinary(config);
	}
}
