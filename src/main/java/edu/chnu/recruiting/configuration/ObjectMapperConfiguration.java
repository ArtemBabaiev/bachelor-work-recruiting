package edu.chnu.recruiting.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfiguration {
	@Bean
	public JavaTimeModule jsr310Module() {
		return new JavaTimeModule();
	}
}
