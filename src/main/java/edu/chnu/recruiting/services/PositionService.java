package edu.chnu.recruiting.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.repositories.PositionRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.ui.views.management.position.components.FormCreationComponent;
import jakarta.transaction.Transactional;

@Service
public class PositionService {
	@Autowired
	private WizardService wizardService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Position createPosition(Position position, FormCreationComponent formComponent) {
		Wizard wizard = this.wizardService.createWizard(formComponent);
		Company company = companyService.getCompanyByUser(securityContext.getAuthenticatedUser());
		position.setWizardData(wizard);
		position.setCompany(company);
		position.setDatePosted(LocalDate.now());
		return positionRepository.save(position);

	}

	public List<PositionViewModel> getAllBy(Specification<Position> specification, Pageable page) {
		return this.positionRepository.findAll(specification, page).getContent().stream()
				.map(e -> modelMapper.map(e, PositionViewModel.class)).collect(Collectors.toList());
	}

	public long countBy(Specification<Position> specification) {
		return this.positionRepository.count(specification);
	}

	public Position getPosition(Long id) {
		return this.positionRepository.findById(id).orElse(null);
	}

	public <T> T getPosition(Long id, Class<T> modelType) {
		Position model = this.positionRepository.findById(id).orElse(null);
		if (model == null) {
			return null;
		}
		return modelMapper.map(model, modelType);
	}

	@Transactional
	public void deactivatePosition(Long id) {
		this.positionRepository.setActiveWhereId(id, false);
	}

	@Transactional
	public void activatePosition(Long id) {
		this.positionRepository.setActiveWhereId(id, true);
	}

	public List<Position> getByCurrentCompany() {
		Company comp = this.companyService.getCompanyByUser(this.securityContext.getAuthenticatedUser());
		return this.positionRepository.findByCompany(comp);
	}

	public Position updatePosition(Position position) {
		position.setUpdatedAt(LocalDateTime.now());
		return positionRepository.save(position);
	}

	public Position updateWizard(Long positionId, FormCreationComponent form) {
		Position position = this.positionRepository.findById(positionId).get();
		position.setWizardData(this.wizardService.createWizard(form));
		position.setUpdatedAt(LocalDateTime.now());
		return positionRepository.save(position);
	}

	public long countActivePositions() {
		return this.positionRepository.countByActiveIsTrue();
	}
}
