package edu.chnu.recruiting.front.views.position;

import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.components.position.FieldsToolbar;
import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.PositionService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Create Position")
@Route(value = "position-create", layout = MainLayout.class)
@RolesAllowed({"COMPANY", "RECRUITER"})
public class PositionCreateView extends VerticalLayout {
	Binder<Position> binder = new BeanValidationBinder<Position>(Position.class);
	Position model = new Position();

	TabSheet tabSheet = new TabSheet();

	TextField name = new TextField("Position name");
	TextArea description = new TextArea("Description");

	FieldsToolbar toolbar = new FieldsToolbar();
	FormСreationComponent form = new FormСreationComponent();

	
	Button createPositionBtn = new Button("Create position");

	PositionService positionService;

	public PositionCreateView(PositionService positionService) {
		this.positionService = positionService;
		this.configureBinder();
		this.configureComponents();
		tabSheet.add("Position Info", this.getPositionInfoSheet());
		tabSheet.add("Form", this.getFormSheet());
		tabSheet.add("Complete", this.getCompleteSheet());
		
		tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);
		tabSheet.setSizeFull();
		add(tabSheet);

	}

	private void configureComponents() {
		name.setValueChangeMode(ValueChangeMode.EAGER);
		form.setWidthFull();
		createPositionBtn.addClickListener(e -> this.positionService.createPosition(binder.getBean(), form));
	}

	private void configureBinder() {
		binder.bindInstanceFields(this);
		binder.addStatusChangeListener(e -> createPositionBtn.setEnabled(binder.isValid()));
		binder.setBean(model);
	}

	private Component getPositionInfoSheet() {
		VerticalLayout infoSheet = new VerticalLayout();
		infoSheet.setSizeFull();
		infoSheet.setAlignItems(Alignment.CENTER);
		infoSheet.add(name, description);
		this.setWidth("315px", name, description);
		return infoSheet;
	}

	private Component getFormSheet() {
		VerticalLayout sheet = new VerticalLayout();
		VerticalLayout formCanvas = new VerticalLayout(getNote(), form);
		HorizontalLayout formSetup = new HorizontalLayout(formCanvas, toolbar);
		toolbar.setWidth("350px");
		formSetup.addClassNames("content");
		formSetup.setSizeFull();
		sheet.add(formSetup);
		//formCanvas.setAlignItems(Alignment.CENTER);
		return sheet;
	}
	
	private Component getCompleteSheet() {
		var vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setAlignItems(Alignment.CENTER);
		vl.add(createPositionBtn);
		return vl;
	}
	
	private Component getNote() {
		Span note = new Span("First name, Last name and Date of birth are mandantory data and will be automatically created");
		Span iconSpan = new Span();
		
		Icon icon = VaadinIcon.WARNING.create();
		icon.getStyle().set("padding", "var(--lumo-space-xs)");
		iconSpan.add(icon);
		
		note.addClassNames(LumoUtility.Padding.Horizontal.SMALL);
		
		iconSpan.getElement().getThemeList().add("badge error");
		
		return new Span(iconSpan, note);
	}
	
	private void setWidth(String width, HasSize... components) {
		Stream.of(components).forEach(comp -> comp.setWidth(width));
	}
}
