package edu.chnu.recruiting.models.wizard;

import lombok.Data;

@Data
public class WizardInput {
	private String name;
	private ValueType type;
	private Object value;
}
