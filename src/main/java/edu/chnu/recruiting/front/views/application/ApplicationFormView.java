package edu.chnu.recruiting.front.views.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.server.VaadinRequest;

import edu.chnu.recruiting.exceptions.ApplicationNonEditableException;
import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.application.SectionForm.BackEvent;
import edu.chnu.recruiting.front.views.application.SectionForm.NextEvent;
import edu.chnu.recruiting.front.views.apply.ApplicationFormModel;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardField;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.utils.enums.ApplicationStatuses;
import jakarta.annotation.security.PermitAll;

@Route(value = "application-form", layout = MainLayout.class)
@PageTitle("Application Form")
@PermitAll
public class ApplicationFormView extends VerticalLayout implements BeforeEnterObserver {
	private Binder<Wizard> binder = new BeanValidationBinder<Wizard>(Wizard.class);

	private ApplicationService applicationService;

	private UUID applicationId;

	private Application application;
	
	private Component currentSection;

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
		application = this.applicationService.getApplication(applicationId);
		if (!ApplicationStatuses.editable(application.getStatus())) {
			throw new ApplicationNonEditableException();
		}
		initComponent();
	}

	private void initComponent() {
		setSection(application.getWizardData().getCurrentStep());
		

	}
	
	private void setSection(Integer step) {
		if (currentSection != null) {
			this.remove(currentSection);
		}
		if (step == null) {
			currentSection = getSection(application.getWizardData().getStep(0));
		} else {
			currentSection = getSection(application.getWizardData().getStep(step));
		}
		
		add(currentSection);
	}

	private Component getSection(WizardStep step) {
		var section = new SectionForm(step);
		section.addNextListener(e -> handleNextEvent(e));
		section.addBackListener(e -> handleBackEvent(e));
		return section;
	}

	private void handleBackEvent(BackEvent e) {
		var currentStep = application.getWizardData().getCurrentStep();
		application.getWizardData().setCurrentStep(--currentStep);
		this.setSection(currentStep);
	}

	private void handleNextEvent(NextEvent e) {
		if (e.getStepId() == application.getWizardData().getTotalSteps() - 1) {
			application = this.applicationService.saveFinalApplication(application, e.getStepId());
			UI.getCurrent().navigate(ApplicationView.class, new RouteParam("id", application.getId().toString()));
		} else {
			application = this.applicationService.saveApplication(application, e.getStepId());
			setSection(application.getWizardData().getCurrentStep());
		}
	}
}
