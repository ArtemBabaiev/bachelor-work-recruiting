package edu.chnu.recruiting.front.views.application;

import java.util.UUID;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.services.ApplicationService;
import jakarta.annotation.security.PermitAll;

@Route(value = "application-form", layout = MainLayout.class)
@PageTitle("Application Form")
@PermitAll
public class ApplicationFormView extends VerticalLayout implements BeforeEnterObserver {

	private ApplicationService applicationService;
	
	private UUID applicationId;
	
	private Wizard wizard;
	
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
		wizard = this.applicationService.getApplication(applicationId).getWizardData();
		initComponent();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		
	}
}
