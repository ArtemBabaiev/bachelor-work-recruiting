package edu.chnu.recruiting.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.WizardFinishedException;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.ApplicationFull;
import edu.chnu.recruiting.models.ApplicationSummary;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.formModels.ApplyFormModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.repositories.ApplicationFullRepository;
import edu.chnu.recruiting.repositories.ApplicationRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.transaction.Transactional;

@Service
public class ApplicationService {
	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationFullRepository appFullRepository;

	@Autowired
	private PositionService positionService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ModelMapper modelMapper;

	public Application apply(ApplyFormModel model, Long positionId) {
		Position position = positionService.getPosition(positionId);
		User user = securityContext.getAuthenticatedUser();
		if (!position.getActive()) {
			throw new ForbiddenException();
		}
		ApplicationFull entity = modelMapper.map(model, ApplicationFull.class);
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
		return this.appFullRepository.save(entity);
	}

	public ApplicationFull getApplicationForm(Long id) {
		return this.appFullRepository.findById(id).orElse(null);
	}

	public Application saveFinalApplication(ApplicationFull app) {
		app.setStatus(ApplicationStatus.PENDING_REVIEW.toString());
		app.setSubmittedAt(LocalDateTime.now());
		return this.appFullRepository.save(app);
	}

	public WizardStep getApplicationStep(Long applicationId, int stepId) {
		Wizard wizard = this.getApplicationForm(applicationId).getWizardData();
		return wizard.getStep(stepId);
	}

	public WizardStep saveStepAndGetNext(Long applicationId, WizardStep step) throws WizardFinishedException {
		ApplicationFull app = getApplicationForm(applicationId);
		Wizard wizard = app.getWizardData();
		wizard.updateStep(step);
		int newStep = step.getId() + 1;
		wizard.setCurrentStep(newStep);
		if (newStep == wizard.getTotalSteps()) {
			this.saveFinalApplication(app);
			throw new WizardFinishedException();
		} else {
			return this.appFullRepository.save(app).getWizardData().getStep(newStep);
		}
	}

	public List<ApplicationSummary> getAllBy(Specification<ApplicationSummary> specification, Pageable page) {
		return this.applicationRepository.findAll(specification, page).getContent();
	}

	public <T> List<T> getAllBy(Specification<ApplicationSummary> specification, Pageable page, Class<T> modelType) {
		return getAllBy(specification, page).stream().map(e -> modelMapper.map(e, modelType))
				.collect(Collectors.toList());
	}

	public long countBy(Specification<ApplicationSummary> specification) {
		return this.applicationRepository.count(specification);
	}

	public <T> T getApplicationFull(Long id, Class<T> modelType) {
		var entity = this.appFullRepository.findById(id).orElse(null);
		if (entity == null) {
			return null;
		}
		return modelMapper.map(entity, modelType);
	}

	@Transactional
	public void acceptApplication(Long id) {
		this.applicationRepository.updateStatus(id, ApplicationStatus.ACCEPTED.toString());
	}

	@Transactional
	public void rejectApplication(Long id, String rejectReason) {
		this.applicationRepository.updateStatusAndReason(id, ApplicationStatus.REJECTED.toString(), rejectReason);
	}

}
