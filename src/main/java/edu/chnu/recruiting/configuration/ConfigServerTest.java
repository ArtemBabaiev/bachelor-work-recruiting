package edu.chnu.recruiting.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Getter
@RefreshScope
public class ConfigServerTest {

	@Value("${test}")
	private String test;
	
//	@Value("${test2}")
//	private String test2;
	
	@PostConstruct
	public void init() {
		log.info(test);
		//log.info(test2);
	}
}
