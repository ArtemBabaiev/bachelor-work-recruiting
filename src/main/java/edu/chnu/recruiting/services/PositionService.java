package edu.chnu.recruiting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.wizard.Wizard;

@Service
public class PositionService {
	@Autowired
	private WizardService wizardService;
	
	public void createPosition(Position position, FormСreationComponent formComponent) {
		Wizard wizard = this.wizardService.createWizard(formComponent);
		position.setWizardData(wizard);
		System.out.println();
	}
}
