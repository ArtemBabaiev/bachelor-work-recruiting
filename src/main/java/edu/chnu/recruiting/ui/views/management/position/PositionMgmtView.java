package edu.chnu.recruiting.ui.views.management.position;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.AccessService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.management.position.components.PositionForm;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Position Management")
@Route(value = "management/positions/:id", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionMgmtView extends VerticalLayout implements BeforeEnterObserver {
	private Button activationBtn = new Button();
	private Button editFormBtn = new Button("Edit form");
	private Button backBtn = new Button(VaadinIcon.ARROW_LEFT.create(), e -> UI.getCurrent().navigate(PositionsMgmtView.class));
	private Position model;
	private PositionForm form;
	private PositionService positionService;
	private AccessService accessService;

	public PositionMgmtView(PositionService positionService, AccessService accessService) {
		this.positionService = positionService;
		this.accessService = accessService;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			Long id = event.getRouteParameters().getLong("id").get();
			model = positionService.getPosition(id);
		} catch (Exception e) {
			event.rerouteToError(BadRequestException.class);
			return;
		}
		if (model == null) {
			event.rerouteToError(ResourceNotFoundException.class);
		} else if (!accessService.canUserManagePosition(model)) {
			event.rerouteToError(ForbiddenException.class);
		} else {
			initComponent();
		}
	}

	private void initComponent() {

		form = new PositionForm(model);
		this.setSizeFull();
		configureComponents();
		form.setMaxWidth("800px");
		Span space = new Span();
		var controls = new HorizontalLayout(backBtn, space, activationBtn, editFormBtn);
		controls.expand(space);
		controls.setWidthFull();
		Div test = new Div(controls, form);
		test.setMaxWidth("800px");
		test.addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.CENTER, AlignItems.CENTER);
		this.setAlignItems(Alignment.CENTER);
		add(test);
	}

	private void configureComponents() {
		editFormBtn.addClickListener(
				e -> UI.getCurrent().navigate(PositionFormEditView.class, new RouteParam("id", model.getId())));

		form.setResponsiveSteps(new ResponsiveStep("0", 1));
		form.addSaveListener(this::handleSaveEvent);
		form.addCancelListener(this::handleCancelEvent);

		activationBtn.setText(model.getActive() ? "Deactivate" : "Activate");
		activationBtn.addThemeVariants(model.getActive() ? ButtonVariant.LUMO_ERROR : ButtonVariant.LUMO_SUCCESS);
		activationBtn.addClickListener(e -> {
			if (model.getActive()) {
				positionService.deactivatePosition(model.getId());
			} else {
				positionService.activatePosition(model.getId());
			}
			UI.getCurrent().getPage().reload();
		});
	}

	private void handleSaveEvent(PositionForm.SaveEvent e) {
		model = this.positionService.updatePosition(e.getModel());
		form.setBean(model);
	}

	private void handleCancelEvent(PositionForm.CancelEvent e) {
		model = positionService.getPosition(model.getId());
		form.setBean(model);
	}
}
