package edu.chnu.recruiting.models.wizard;

import java.util.List;

import lombok.Data;

@Data
public class WizardField {
	private String id;
	private String labelKey;
	private ValueType type;
	private Object userValue;
	
	//in case checkbox, or dropdown list
	List<String> options;
}
