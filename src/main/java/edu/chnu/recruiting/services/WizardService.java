package edu.chnu.recruiting.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.front.components.position.SectionComponent;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;

@Service
public class WizardService {

	public Wizard createWizard(FormСreationComponent form) {
		List<SectionComponent> sections = form.getSections();
		Wizard wizard = new Wizard();
		int sectionIndex = 0;
		for (SectionComponent sectionComponent : sections) {
			WizardStep step = sectionComponent.getStep();
			step.setOrderIndex(sectionIndex++);
			step.setId(UUID.randomUUID().toString());
			sectionComponent.getQuestionsComponents().forEach(q -> step.addField(q.getField()));
			wizard.addStep(step);
		}
		return wizard;
	}
}
