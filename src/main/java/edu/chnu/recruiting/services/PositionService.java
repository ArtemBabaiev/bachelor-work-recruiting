package edu.chnu.recruiting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.repositories.PositionRepository;

@Service
public class PositionService {
	@Autowired
	private WizardService wizardService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private PositionRepository positionRepository;
	
	public void createPosition(Position position, FormСreationComponent formComponent) {
		try {
			Wizard wizard = this.wizardService.createWizard(formComponent);
			Company company = companyService.getCompanyByUser(userService.getAuthenticatedUser());
			position.setWizardData(wizard);
			position.setCompany(company);
			positionRepository.save(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
