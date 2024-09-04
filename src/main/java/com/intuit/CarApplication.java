package com.intuit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class CarApplication extends SpringBootServletInitializer {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	public static void main(String[] args) {
		System.setProperty("spring.profiles.active","dev");
		SpringApplication.run(CarApplication.class, args);
	}

}
