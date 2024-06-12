package edu.chnu.recruiting.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.ui.views.management.position.components.FormCreationComponent;
import edu.chnu.recruiting.ui.views.management.position.components.QuestionComponent;
import edu.chnu.recruiting.ui.views.management.position.components.SectionComponent;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WizardService {

	@Autowired
	private GcTextToSpeechService ttsService;

	public Wizard createWizard(FormCreationComponent form) {
		List<SectionComponent> sections = form.getSections();
		Wizard wizard = new Wizard();
		int sectionIndex = 0;
		for (SectionComponent sectionComponent : sections) {
			int questionIndex = 0;
			WizardStep step = sectionComponent.getStep();
			step.getFields().clear();
			step.setId(sectionIndex++);
			List<QuestionComponent> questions = sectionComponent.getQuestionsComponents();
			for (QuestionComponent questionComponent : questions) {
				var field = questionComponent.getField().withId(questionIndex++);
				if (field.isTextToSpeech()) {
					try {
						field.setSpeech(this.ttsService.getSpeech(field.getQuestion()));
					} catch (IOException e) {
						log.error("Error occured getting getting speech for field", e);
						field.setSpeech(new byte[0]);
					}
				}
				step.addField(questionComponent.getField());
			}
			wizard.addStep(step);
		}
		return wizard;
	}
}
