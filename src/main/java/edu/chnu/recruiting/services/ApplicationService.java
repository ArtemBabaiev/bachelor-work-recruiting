package edu.chnu.recruiting.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.WizardFinishedException;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.formModels.ApplicationFormModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.viewModels.ApplicationGridVM;
import edu.chnu.recruiting.models.viewModels.ApplicationViewModel;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.repositories.ApplicationRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.transaction.Transactional;

@Service
public class ApplicationService {
	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private PositionService positionService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ModelMapper modelMapper;

	public Application apply(ApplicationFormModel model, Long positionId) {
		User user = securityContext.getAuthenticatedUser();
		Position position = positionService.getPosition(positionId);
		if (!position.getActive()) {
			throw new ForbiddenException();
		}
		Application entity = modelMapper.map(model, Application.class);
		entity.setUser(user);
		entity.setPosition(position);
		entity.setWizardData(position.getWizardData());
		var numberOfQuestions = position.getWizardData().getSteps().stream().flatMap(s -> s.getFields().stream())
				.count();
		if (numberOfQuestions > 0) {
			entity.setStatus(ApplicationStatus.PENDING_DATA.toString());
		} else {
			entity.setStatus(ApplicationStatus.PENDING_REVIEW.toString());
			entity.setSubmittedAt(LocalDateTime.now());
		}
		return this.applicationRepository.save(entity);
	}

	public Application getApplicationForm(UUID id) {
		return this.applicationRepository.findById(id).orElse(null);
	}

	public Application saveApplication(Application app, int filledStep) {
		app.getWizardData().setCurrentStep(filledStep + 1);
		return this.applicationRepository.save(app);
	}

	public Application saveFinalApplication(Application app) {
		app.setStatus(ApplicationStatus.PENDING_REVIEW.toString());
		app.setSubmittedAt(LocalDateTime.now());
		return this.applicationRepository.save(app);
	}

	public WizardStep getApplicationStep(UUID applicationId, int stepId) {
		Wizard wizard = this.getApplicationForm(applicationId).getWizardData();
		return wizard.getStep(stepId);
	}

	public WizardStep saveStepAndGetNext(UUID applicationId, WizardStep step) throws WizardFinishedException {
		Application app = getApplicationForm(applicationId);
		Wizard wizard = app.getWizardData();
		wizard.updateStep(step);
		int newStep = step.getId() + 1;
		wizard.setCurrentStep(newStep);
		if (newStep == wizard.getTotalSteps()) {
			this.saveFinalApplication(app);
			throw new WizardFinishedException();
		} else {
			return this.applicationRepository.save(app).getWizardData().getStep(newStep);
		}
	}

	public List<ApplicationGridVM> getAllBy(Specification<Application> specification, Pageable page) {
		return this.applicationRepository.findAll(specification, page).getContent().stream()
				.map(e -> modelMapper.map(e, ApplicationGridVM.class)).collect(Collectors.toList());
	}

	public long countBy(Specification<Application> specification) {
		return this.applicationRepository.count(specification);
	}

	public ApplicationViewModel getApplicationVM(UUID id) {
		var entity = this.applicationRepository.findById(id).orElse(null);
		if (entity == null) {
			return null;
		}
		return modelMapper.map(entity, ApplicationViewModel.class);
	}

	@Transactional
	public void acceptApplication(UUID id) {
		this.applicationRepository.updateStatus(id, ApplicationStatus.ACCEPTED.toString());
	}

	@Transactional
	public void rejectApplication(UUID id, String rejectReason) {
		this.applicationRepository.updateStatusAndReason(id, ApplicationStatus.REJECTED.toString(), rejectReason);
	}

}
