package edu.chnu.recruiting.ui.views.management.position;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.services.AccessService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.management.position.components.FormCreationComponent;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Position form")
@Route(value = "management/positions/:id/form", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionFormEditView extends VerticalLayout implements BeforeEnterObserver {

	private PositionService positionService;
	private AccessService accessService;

	Long positionId;
	private Wizard model;

	private FormCreationComponent form;
	private Button saveBtn = new Button("Save");
	private Button backBtn = new Button(VaadinIcon.ARROW_LEFT.create());

	public PositionFormEditView(ServiceManager sm) {
		this.positionService = sm.getPositionService();
		this.accessService = sm.getAccessService();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Position pos;
		try {
			positionId = event.getRouteParameters().getLong("id").get();
			pos = positionService.getPosition(positionId);
		} catch (Exception e) {
			event.rerouteToError(BadRequestException.class);
			return;
		}
		if (pos == null) {
			event.rerouteToError(ResourceNotFoundException.class);
		} else if (!accessService.canUserManagePosition(pos)) {
			event.rerouteToError(ForbiddenException.class);
		} else {
			model = pos.getWizardData();
			initComponent();
		}
	}

	private void initComponent() {
		setAlignItems(Alignment.CENTER);
		setSizeFull();

		form = new FormCreationComponent(model);
		form.setMaxWidth("800px");

		saveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		
		saveBtn.addClickListener(e -> handleSaveBtnClick(e));
		backBtn.addClickListener(
				e -> UI.getCurrent().navigate(PositionMgmtView.class, new RouteParam("id", positionId)));

		HorizontalLayout controls = new HorizontalLayout(backBtn, saveBtn);
		controls.setWidthFull();
		controls.setMaxWidth("800px");
		controls.setJustifyContentMode(JustifyContentMode.BETWEEN);
		add(controls, form);

	}

	private void handleSaveBtnClick(ClickEvent<Button> e) {
		this.positionService.updateWizard(positionId, form);
		UI.getCurrent().getPage().reload();
	}

}
