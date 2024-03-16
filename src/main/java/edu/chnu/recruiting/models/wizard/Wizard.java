package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Wizard {
	private String currentStep;
	private List<WizardStep> steps = new ArrayList<WizardStep>();

	public void addStep(WizardStep step) {
		this.steps.add(step);
	}
}
