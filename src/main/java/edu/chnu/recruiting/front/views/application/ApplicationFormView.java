package edu.chnu.recruiting.front.views.application;

import java.util.UUID;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;

import edu.chnu.recruiting.exceptions.ApplicationNonEditableException;
import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.WizardFinishedException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.application.SectionForm.BackEvent;
import edu.chnu.recruiting.front.views.application.SectionForm.NextEvent;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.PermitAll;

@Route(value = "application-form", layout = MainLayout.class)
@PageTitle("Application Form")
@PermitAll
public class ApplicationFormView extends VerticalLayout implements BeforeEnterObserver {

	private ApplicationService applicationService;

	private UUID applicationId;

	private SectionForm currentSection;

	public ApplicationFormView(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		var optId = event.getLocation().getQueryParameters().getSingleParameter("id");
		if (optId.isEmpty()) {
			throw new BadRequestException();
		}
		applicationId = UUID.fromString(optId.get());
		Application application = this.applicationService.getApplicationForm(applicationId);
		if (!ApplicationStatus.editable(application.getStatus())) {
			throw new ApplicationNonEditableException();
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

		add(currentSection);
	}

	private void handleBackEvent(BackEvent e) {
		this.updateSectionComponent(this.applicationService.getApplicationStep(applicationId, e.getStep().getId() - 1));
	}

	private void handleNextEvent(NextEvent e) {
		try {
			updateSectionComponent(this.applicationService.saveStepAndGetNext(applicationId, e.getStep())) ;
		} catch (WizardFinishedException e2) {
			UI.getCurrent().navigate(ApplicationView.class, new RouteParam("id", applicationId.toString()));
		}
	}
}
