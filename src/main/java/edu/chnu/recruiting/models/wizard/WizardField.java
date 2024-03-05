package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WizardField {
	private String id;
	private String question;
	private ValueType type;
	private Object userValue;
	
	//in case checkbox, or dropdown list
	private List<String> options = new ArrayList<String>();
	
	public void addOption(String option) {
		this.options.add(option);
	}
}
