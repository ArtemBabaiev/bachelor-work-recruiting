package edu.chnu.recruiting.services;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.chnu.recruiting.front.views.management.position.components.FormСreationComponent;
import edu.chnu.recruiting.front.views.management.position.components.QuestionComponent;
import edu.chnu.recruiting.front.views.management.position.components.SectionComponent;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;

@Service
public class WizardService {

	public Wizard createWizard(FormСreationComponent form) {
		List<SectionComponent> sections = form.getSections();
		Wizard wizard = new Wizard();
		int sectionIndex = 0;
		for (SectionComponent sectionComponent : sections) {
			int questionIndex = 0;
			WizardStep step = sectionComponent.getStep();
			step.setId(sectionIndex++);
			List<QuestionComponent> questions = sectionComponent.getQuestionsComponents();
			for (QuestionComponent questionComponent : questions) {
				step.addField(questionComponent.getField().withId(questionIndex++));
			}
			wizard.addStep(step);
		}
		return wizard;
	}
}
