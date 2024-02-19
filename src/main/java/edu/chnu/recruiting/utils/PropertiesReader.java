package edu.chnu.recruiting.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class PropertiesReader {
	@Value("${spring.application.name}")
	private String applicationName;
	
}
