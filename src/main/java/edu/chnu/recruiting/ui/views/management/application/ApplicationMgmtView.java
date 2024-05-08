package edu.chnu.recruiting.ui.views.management.application;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.models.viewModels.ApplicationViewModel;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.services.AccessService;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.components.AudioTag;
import edu.chnu.recruiting.ui.components.DownloadComponent;
import edu.chnu.recruiting.ui.views.management.application.SectionDataComponent.FieldDataComponent;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Application Management")
@Route(value = "management/applications/:appId", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class ApplicationMgmtView extends VerticalLayout implements BeforeEnterObserver {
	private ApplicationService applicationService;
	private AccessService accessService;

	private Long appId;

	private ApplicationViewModel model;

	private Button backBtn = new Button(VaadinIcon.ARROW_LEFT.create(),
			e -> UI.getCurrent().getPage().getHistory().back());

	public ApplicationMgmtView(ServiceManager uow) {
		this.applicationService = uow.getApplicationService();
		this.accessService = uow.getAccessService();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			this.appId = Long.parseLong(event.getRouteParameters().get("appId").get());
			model = applicationService.getApplicationFull(appId, ApplicationViewModel.class);
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
		HorizontalLayout controls = new HorizontalLayout(backBtn);
		backBtn.addThemeVariants(ButtonVariant.LUMO_ICON);
		Span badge = ApplicationStatus.getBadge(model.getStatus());
		badge.setText("Status: " + badge.getText());
		badge.getStyle().set("font-size", "var(--lumo-font-size-m)");
		if (model.getStatus().equals(ApplicationStatus.REJECTED.toString())) {
			Tooltip.forComponent(badge).withText(model.getRejectReason());
		}
		Div d = new Div(badge);
		controls.addAndExpand(d);
		Button acceptBtn = new Button("Accept", e -> handleAcceptClick(e));
		Button rejectBtn = new Button("Reject", e -> handleRejectClick(e));
		if (model.getStatus().equals(ApplicationStatus.ACCEPTED.toString())) {
			acceptBtn.setDisableOnClick(true);
			acceptBtn.setEnabled(false);
		} else if (model.getStatus().equals(ApplicationStatus.REJECTED.toString())) {
			rejectBtn.setDisableOnClick(true);
			rejectBtn.setEnabled(false);
		}
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
		fs.add(new FieldDataComponent("Full name", model.getFullName()));
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
					var audio = new AudioTag((byte[]) field.getUserValue());
					audio.addClassName(LumoUtility.Margin.Top.MEDIUM);
					fs.add(new FieldDataComponent(field.getQuestion(), audio));
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

	private void handleAcceptClick(ClickEvent<Button> e) {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Accept candidate");
		dialog.setWidth("450px");

		VerticalLayout vl = new VerticalLayout();
		vl.addClassNames(LumoUtility.Padding.NONE);
		vl.setSizeFull();
		dialog.add(vl);

		TextArea input = new TextArea("Notes for candidate: ");
		input.setRequired(true);
		input.setWidthFull();
		vl.add(input);

		dialog.getFooter().add(new Button("Cancel", e1 -> dialog.close()));
		dialog.getFooter().add(new Button("Confirm", e1 -> {
			var value = input.getValue();
			if (value != null && !value.isBlank()) {
				this.applicationService.acceptApplication(appId, value);
				UI.getCurrent().getPage().reload();
			} else {
				input.setInvalid(true);
			}
		}));
		dialog.open();
	}

	private void handleRejectClick(ClickEvent<Button> e) {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Reject candidate");
		dialog.setWidth("450px");

		VerticalLayout vl = new VerticalLayout();
		vl.addClassNames(LumoUtility.Padding.NONE);
		vl.setSizeFull();
		dialog.add(vl);

		TextArea input = new TextArea("Reason of rejection");
		input.setRequired(true);
		input.setWidthFull();
		vl.add(input);

		dialog.getFooter().add(new Button("Cancel", e1 -> dialog.close()));
		dialog.getFooter().add(new Button("Confirm", e1 -> {
			var value = input.getValue();
			if (value != null && !value.isBlank()) {
				applicationService.rejectApplication(appId, input.getValue());
				UI.getCurrent().getPage().reload();
			} else {
				input.setInvalid(true);
			}
		}));
		dialog.open();
	}

}
