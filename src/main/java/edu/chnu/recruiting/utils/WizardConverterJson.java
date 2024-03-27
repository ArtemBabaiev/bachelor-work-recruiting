package edu.chnu.recruiting.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.chnu.recruiting.models.wizard.Wizard;
import jakarta.persistence.AttributeConverter;

public class WizardConverterJson implements AttributeConverter<Wizard, String> {
	private final static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper().findAndRegisterModules();
		objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, 
				false);
	}

	@Override
	public String convertToDatabaseColumn(Wizard meta) {
		try {
			return objectMapper.writeValueAsString(meta);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Wizard convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, Wizard.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
