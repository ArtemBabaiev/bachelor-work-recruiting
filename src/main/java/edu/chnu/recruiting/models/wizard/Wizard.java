package edu.chnu.recruiting.models.wizard;

import java.util.List;

import lombok.Data;

@Data
public class Wizard {
	private String currentStep;
	List<WizardStep> steps;
}
