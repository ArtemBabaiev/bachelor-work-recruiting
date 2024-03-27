package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Wizard {
	private String currentStep;
	private List<WizardStep> steps = new ArrayList<WizardStep>();

	public void addStep(WizardStep step) {
		this.steps.add(step);
	}
}
