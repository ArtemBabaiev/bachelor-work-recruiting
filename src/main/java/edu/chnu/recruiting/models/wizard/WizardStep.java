package edu.chnu.recruiting.models.wizard;

import java.util.List;

import lombok.Data;

@Data
public class WizardStep {
	private int order;
	private String name;
	private List<WizardInput> inputs;
}
