package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WizardField {
	private String question;
	private ValueType type;
	private Object userValue;
	private boolean required = false;
	private boolean textToSpeech = false;
	
	
	//in case checkbox, or dropdown list
	private List<String> options = new ArrayList<String>();
	
	public void addOption(String option) {
		this.options.add(option);
	}
	
	public void resetOptional() {
		options.clear();
		
	}
}
