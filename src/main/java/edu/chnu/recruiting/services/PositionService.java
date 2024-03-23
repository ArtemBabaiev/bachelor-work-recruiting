package edu.chnu.recruiting.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.flow.router.InternalServerError;
import com.vaadin.flow.router.NotFoundException;

import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.repositories.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PositionService {
	@Autowired
	private WizardService wizardService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private PositionRepository positionRepository;
	
	public Position createPosition(Position position, FormСreationComponent formComponent) {
		try {
			Wizard wizard = this.wizardService.createWizard(formComponent);
			Company company = companyService.getCompanyByUser(userService.getAuthenticatedUser());
			position.setWizardData(wizard);
			position.setCompany(company);
			return positionRepository.save(position);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public List<Position> getFilteredPositions(String name, Pageable page) {
		log.info("Page: {}; Size {}", page.getPageNumber(), page.getPageSize());
		return this.positionRepository.findByNameContains(name, page).getContent();
	}
	
	public List<Position> getFilteredPositionsCount(String name, Pageable page) {
		log.info("Page: {}; Size {}", page.getPageNumber(), page.getPageSize());
		return this.positionRepository.findByNameContains(name, page).getContent();
	}
	
	@Transactional
	public Position getPosition(Long id) {
		return this.positionRepository.findById(id).orElseThrow(() -> new NotFoundException("Position not found"));
	}
}
