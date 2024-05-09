package edu.chnu.recruiting.ui.views.application;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.exceptions.WizardFinishedException;
import edu.chnu.recruiting.models.ApplicationFull;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.services.AccessService;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.application.SectionForm.BackEvent;
import edu.chnu.recruiting.ui.views.application.SectionForm.NextEvent;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.PermitAll;

@Route(value = "application/form", layout = MainLayout.class)
@PageTitle("Application Form")
@PermitAll
public class ApplicationFormView extends VerticalLayout implements BeforeEnterObserver {

	private ApplicationService applicationService;
	private AccessService accessService;

	private Long applicationId;

	private SectionForm currentSection;

	public ApplicationFormView(ServiceManager uow) {
		this.applicationService = uow.getApplicationService();
		this.accessService = uow.getAccessService();
		this.setSizeFull();
		this.setAlignItems(Alignment.CENTER);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			var optId = event.getLocation().getQueryParameters().getSingleParameter("id");
			applicationId = Long.parseLong(optId.get());
		} catch (Exception e) {
			event.rerouteToError(BadRequestException.class);
			return;
		}

		ApplicationFull application = this.applicationService.getApplicationForm(applicationId);
		if (application == null) {
			event.rerouteToError(ResourceNotFoundException.class);
			return;
		}
		if (!this.accessService.canUserEditApplication(application)
				|| !application.getStatus().equals(ApplicationStatus.PENDING_DATA.toString())) {
			event.rerouteToError(ForbiddenException.class);
			return;
		}
		if (application.getWizardData().getCurrentStep() == null) {
			updateSectionComponent(application.getWizardData().getStep(0));
		} else {
			updateSectionComponent(application.getWizardData().getStep(application.getWizardData().getCurrentStep()));
		}
	}

	private void updateSectionComponent(WizardStep step) {
		if (currentSection != null) {
			this.remove(currentSection);
		}

		currentSection = new SectionForm(step);
		currentSection.addNextListener(e -> handleNextEvent(e));
		currentSection.addBackListener(e -> handleBackEvent(e));
		currentSection.setMaxWidth("550px");
		add(currentSection);
	}

	private void handleBackEvent(BackEvent e) {
		this.updateSectionComponent(this.applicationService.getApplicationStep(applicationId, e.getStep().getId() - 1));
	}

	private void handleNextEvent(NextEvent e) {
		try {
			updateSectionComponent(this.applicationService.saveStepAndGetNext(applicationId, e.getStep()));
		} catch (WizardFinishedException e2) {
			UI.getCurrent().navigate(ApplicationSuccessfullView.class);
		}
	}
}
