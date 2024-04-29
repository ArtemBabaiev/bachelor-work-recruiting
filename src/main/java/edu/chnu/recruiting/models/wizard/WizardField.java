package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.time.LocalDate;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WizardField {
	private Integer id;
	
	private String question;
	
	private ValueType type;
	
	@JsonTypeInfo(use = Id.NAME, property = "type", include = As.EXTERNAL_PROPERTY)
	@JsonSubTypes(value = { 
			@JsonSubTypes.Type(value = LocalDate.class, name = "DATE") ,
			@JsonSubTypes.Type(value = Double.class, name = "NUMBER") ,
			@JsonSubTypes.Type(value = String.class, name = "TEXT") ,
			@JsonSubTypes.Type(value = byte[].class, name = "AUDIO") ,
			@JsonSubTypes.Type(value = String.class, name = "SELECTION_SINGLE") ,
			@JsonSubTypes.Type(value = Set.class, name = "SELECTION_MULTIPLE") ,
			@JsonSubTypes.Type(value = byte[].class, name = "UPLOAD")
			})
	private Object userValue;
	private boolean required = false;
	private boolean textToSpeech = false;

	// in case checkbox, or dropdown list
	private Set<String> options = new LinkedHashSet<String>();

	private String fileName;
	
	private byte[] speech;
	
	public void addOption(String option) {
		this.options.add(option);
	}

	public void resetOptional() {
		options.clear();
	}

	public WizardField withId(Integer id) {
		this.setId(id);
		return this;
	}
}
