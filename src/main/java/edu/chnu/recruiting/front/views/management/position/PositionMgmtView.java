package edu.chnu.recruiting.front.views.management.position;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Positions")
@Route(value = "management/positions/:id", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionMgmtView extends VerticalLayout {
	private Button activationBtn = new Button();
	private Button editBtn = new Button("Edit");
	private PositionViewModel model;
	
	private PositionService positionService;
	
	private void configureComponents() {
		activationBtn.setText(model.getActive() ? "Deactivate" : "Activate");
		activationBtn.addClickListener(e -> {
			if (model.getActive()) {
				positionService.deactivatePosition(model.getId());
			} else {
				positionService.activatePosition(model.getId());
			}
			UI.getCurrent().getPage().reload();
		});
		editBtn.addClickListener(e -> {
			Notification.show("Edit click");
		});
	}
}
