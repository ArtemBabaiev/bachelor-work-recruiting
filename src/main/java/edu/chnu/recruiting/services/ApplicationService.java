package edu.chnu.recruiting.services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.router.NotFoundException;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.front.views.apply.ApplicationFormModel;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.repositories.ApplicationRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.utils.enums.ApplicationStatuses;

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
		var numberOfQuestions = position.getWizardData().getSteps().stream().flatMap(s -> s.getFields().stream()).count();
		if (numberOfQuestions > 0) {
			entity.setStatus(ApplicationStatuses.PENDING_DATA.toString());
		} else {
			entity.setStatus(ApplicationStatuses.PENDING_REVIEW.toString());
		}
		return this.applicationRepository.save(entity);
	}
	
	public Application getApplication(UUID id) {
		Application app = this.applicationRepository.findById(id).orElseThrow(() -> new NotFoundException("Application not found"));
		if (!securityContext.getAuthenticatedUser().getId().equals(app.getUser().getId())) {
			throw new ForbiddenException();
		}
		return app;
	}
	
	public Application saveApplication(Application app, int filledStep) {
		app.getWizardData().setCurrentStep(filledStep + 1);
		return this.applicationRepository.save(app);
	}
	
	public Application saveFinalApplication(Application app, int filledStep) {
		app.setStatus(ApplicationStatuses.PENDING_REVIEW.toString());
		return this.applicationRepository.save(app);
	}
}
