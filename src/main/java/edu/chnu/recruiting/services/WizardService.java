package edu.chnu.recruiting.services;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.front.components.position.SectionComponent;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;

@Service
public class WizardService {

	public Wizard createWizard(FormСreationComponent form) {
		List<SectionComponent> sections = form.getChildren().filter(c -> c instanceof SectionComponent)
				.map(c -> (SectionComponent) c).toList();
		Wizard wizard = new Wizard();
		for (SectionComponent sectionComponent : sections) {
			WizardStep step = sectionComponent.getStep();
			sectionComponent.getQuestionsComponents().forEach(q -> step.addField(q.getField()));
			wizard.addStep(step);
		}
		
		return wizard;
	}
}
