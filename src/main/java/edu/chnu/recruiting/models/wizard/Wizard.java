package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Wizard {
	private Integer currentStep;
	private List<WizardStep> steps = new ArrayList<WizardStep>();

	public void addStep(WizardStep step) {
		this.steps.add(step);
	}

	@JsonIgnore
	public WizardStep getStep(int id) {
		return steps.stream().filter(s -> s.getId().equals(id)).findFirst().get();
	}

	@JsonIgnore
	public int getTotalSteps() {
		return steps.size();
	}
	
	public void updateStep(WizardStep step) {
		steps.replaceAll(t -> t.getId().equals(step.getId())? step: t);
	}
}
