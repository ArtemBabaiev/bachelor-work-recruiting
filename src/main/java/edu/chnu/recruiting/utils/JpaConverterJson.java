package edu.chnu.recruiting.utils;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Service
@Converter(autoApply = true)
public class JpaConverterJson implements AttributeConverter<Object, String> {
	private final static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Object meta) {
		try {
			return objectMapper.writeValueAsString(meta);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, Object.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
