package edu.chnu.recruiting.front.views.management.application;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.front.components.AudioTag;
import edu.chnu.recruiting.front.components.DownloadComponent;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.management.application.SectionDataComponent.FieldDataComponent;
import edu.chnu.recruiting.models.viewModels.ApplicationViewModel;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.services.AccessService;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.UnitOfWork;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Applications")
@Route(value = "management/applications/:appId", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class ApplicationMgmtView extends VerticalLayout implements BeforeEnterObserver {
	private ApplicationService applicationService;
	private AccessService accessService;

	private Long appId;

	private ApplicationViewModel model;

	public ApplicationMgmtView(UnitOfWork uow) {
		this.applicationService = uow.getApplicationService();
		this.accessService = uow.getAccessService();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			this.appId = Long.parseLong(event.getRouteParameters().get("appId").get());
			model = applicationService.getApplicationVM(appId);
		} catch (Exception e) {
			event.rerouteToError(BadRequestException.class);
			return;
		}
		if (model == null) {
			event.rerouteToError(ResourceNotFoundException.class);
			return;
		}
		if (!accessService.canUserManageApplication(model)) {
			event.rerouteToError(ForbiddenException.class);
			return;
		}
		initComponent();

	}

	private void initComponent() {
		add(getControls());
		addPersonalInfo();
		addApplicationData(model.getWizardData());
	}

	private Component getControls() {
		HorizontalLayout controls = new HorizontalLayout();
		Div d = new Div(ApplicationStatus.getBadge(model.getStatus()));
		controls.addAndExpand(d);
		Button acceptBtn = new Button("Accept", e -> {
			this.applicationService.acceptApplication(appId);
			UI.getCurrent().getPage().reload();
		});
		Button rejectBtn = new Button("Reject", e -> {
			Dialog dialog = new Dialog();
			dialog.setHeaderTitle("Reject candidate");
			TextArea input = new TextArea("Reason of rejection");
			input.setSizeFull();
			dialog.add(input);

			dialog.getFooter().add(new Button("Cancel", e1 -> dialog.close()));
			dialog.getFooter().add(new Button("Confirm", e1 -> {
				applicationService.rejectApplication(appId, input.getValue());
				UI.getCurrent().getPage().reload();
			}));
			dialog.open();
		});
		rejectBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		acceptBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		controls.add(acceptBtn);
		controls.add(rejectBtn);
		controls.setSizeFull();

		controls.setAlignItems(Alignment.CENTER);
		return controls;
	}

	private void addPersonalInfo() {
		List<FieldDataComponent> fs = new ArrayList<SectionDataComponent.FieldDataComponent>();
		fs.add(new FieldDataComponent("First name", model.getFirstName()));
		fs.add(new FieldDataComponent("Last name", model.getLastName()));
		fs.add(new FieldDataComponent("Date of Birth", model.getDateOfBirth()));
		add(new SectionDataComponent("Personal Info", fs));
	}

	private void addApplicationData(Wizard wizard) {
		for (var step : wizard.getSteps()) {
			List<FieldDataComponent> fs = new ArrayList<SectionDataComponent.FieldDataComponent>();
			for (var field : step.getFields()) {
				if (field.getUserValue() == null) {
					fs.add(new FieldDataComponent(field.getQuestion(), field.getUserValue()));
					continue;
				}
				switch (field.getType()) {
				case AUDIO:
					fs.add(new FieldDataComponent(field.getQuestion(), new AudioTag((byte[]) field.getUserValue())));
					break;
				case UPLOAD:
					fs.add(new FieldDataComponent(field.getQuestion(),
							new DownloadComponent(field.getFileName(), (byte[]) field.getUserValue())));
					break;
				default:
					fs.add(new FieldDataComponent(field.getQuestion(), field.getUserValue()));
					break;
				}
			}
			add(new SectionDataComponent(step.getName(), fs));
		}
	}

}
