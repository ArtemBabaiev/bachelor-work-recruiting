package edu.chnu.recruiting.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardField;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.services.GcTextToSpeechService;
import edu.chnu.recruiting.services.WizardService;
import edu.chnu.recruiting.ui.views.management.position.components.FormCreationComponent;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

	@InjectMocks
	WizardService wizardService;
	@Mock
	private GcTextToSpeechService ttsService;

	@Test
	void testCreateWizard() throws IOException {
		Wizard wizard = new Wizard();
		WizardStep step0 = new WizardStep();
		var field = new WizardField();
		field.setTextToSpeech(true);
		step0.setFields(new ArrayList<WizardField>(List.of(field, new WizardField(), new WizardField())));
		wizard.addStep(step0);
		WizardStep step1 = new WizardStep();
		step1.setFields(new ArrayList<WizardField>(List.of(new WizardField(), new WizardField(), new WizardField())));
		wizard.addStep(step1);
		FormCreationComponent form = new FormCreationComponent(wizard);
		when(this.ttsService.getSpeech(anyString())).thenReturn(new byte[] { 1, 1, 1, 1 });
		var newWizard = wizardService.createWizard(form);
		assertEquals(2, newWizard.getSteps().size());
		assertEquals(3, newWizard.getSteps().get(0).getFields().size());
		assertEquals(2, newWizard.getSteps().get(0).getFields().get(2).getId());
	}
}
