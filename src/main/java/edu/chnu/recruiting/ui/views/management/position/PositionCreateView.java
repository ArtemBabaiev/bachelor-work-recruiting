package edu.chnu.recruiting.ui.views.management.position;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.management.position.components.FormCreationComponent;
import edu.chnu.recruiting.ui.views.management.position.components.PositionForm;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Create Position")
@Route(value = "management/position-create", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionCreateView extends VerticalLayout {

	TabSheet tabSheet = new TabSheet();

	FormCreationComponent form = new FormCreationComponent();

	PositionForm positionForm;

	PositionService positionService;

	public PositionCreateView(PositionService positionService) {
		this.positionService = positionService;
		initComponent();
	}

	private void initComponent() {
		positionForm = new PositionForm(new Position());
		this.configureComponents();
		tabSheet.add("Position Info", this.getPositionInfoSheet());
		tabSheet.add("Application Form", this.getFormSheet());
		tabSheet.setSizeFull();
		this.setSpacing(false);
		tabSheet.setMaxWidth("800px");
		this.setSizeFull();
		this.setAlignItems(Alignment.CENTER);
		tabSheet.setSuffixComponent(positionForm.getSaveButton());
		add(tabSheet);
	}

	private void configureComponents() {
		form.addClassName(LumoUtility.Padding.NONE);

		positionForm.getCancelButton().setVisible(false);
		positionForm.addSaveListener(e -> {
			var pos = this.positionService.createPosition(e.getModel(), form);
			UI.getCurrent().navigate(PositionMgmtView.class, new RouteParam("id", pos.getId()));
			Notification
					.show("Application successfully created", 5000,
							com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		});
	}

	private Component getPositionInfoSheet() {
		positionForm.setResponsiveSteps(new ResponsiveStep("0", 1));
		Div test = new Div(positionForm);
		test.addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.CENTER, AlignItems.CENTER);
		VerticalLayout infoSheet = new VerticalLayout(test);
		infoSheet.setSizeFull();
		infoSheet.setAlignItems(Alignment.CENTER);
		return infoSheet;
	}

	private Component getFormSheet() {
		var note = getNote();
		VerticalLayout sheet = new VerticalLayout(note, form);
		sheet.setAlignItems(Alignment.CENTER);
		sheet.setSizeFull();
		return sheet;
	}

	private Span getNote() {
		Span note = new Span("First name, Last name and Date of birth are automatically collected");
		Span iconSpan = new Span();

		Icon icon = VaadinIcon.WARNING.create();
		icon.getStyle().set("padding", "var(--lumo-space-xs)");
		iconSpan.add(icon);

		note.addClassNames(LumoUtility.Padding.Horizontal.SMALL);

		iconSpan.getElement().getThemeList().add("badge error");

		return new Span(iconSpan, note);
	}
}
