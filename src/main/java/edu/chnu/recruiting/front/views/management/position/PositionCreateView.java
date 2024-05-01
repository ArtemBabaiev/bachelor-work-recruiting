package edu.chnu.recruiting.front.views.management.position;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.management.position.components.FormСreationComponent;
import edu.chnu.recruiting.front.views.management.position.components.PositionForm;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.PositionService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Create Position")
@Route(value = "management/position-create", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionCreateView extends VerticalLayout {

	TabSheet tabSheet = new TabSheet();

	FormСreationComponent form = new FormСreationComponent();

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
		tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);
		tabSheet.setSizeFull();
		HorizontalLayout controls = new HorizontalLayout(positionForm.getSaveButton());
		controls.addClassNames(LumoUtility.Padding.NONE);
		controls.setJustifyContentMode(JustifyContentMode.END);
		controls.setWidthFull();
		this.setSpacing(false);
		add(controls, tabSheet);
	}

	private void configureComponents() {
		form.addClassName(LumoUtility.Padding.NONE);

		positionForm.getCancelButton().setVisible(false);
		positionForm.addSaveListener(e -> {
			var pos = this.positionService.createPosition(e.getModel(), form);
			UI.getCurrent().navigate(PositionMgmtView.class, new RouteParam("id", pos.getId()));
		});
	}

	private Component getPositionInfoSheet() {
		positionForm.setResponsiveSteps(new ResponsiveStep("0", 1));
		Div test = new Div(positionForm);
		test.setMaxWidth("800px");
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
		form.setMaxWidth("900px");
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
