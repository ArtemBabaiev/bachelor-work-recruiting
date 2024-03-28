package edu.chnu.recruiting.front.views.position;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.components.fields.picker.SalaryRange;
import edu.chnu.recruiting.front.components.fields.picker.SalaryRangePicker;
import edu.chnu.recruiting.front.components.position.FieldsToolbar;
import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.utils.UiUtils;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Create Position")
@Route(value = "position-create", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionCreateView extends VerticalLayout {
	Binder<Position> binder = new BeanValidationBinder<Position>(Position.class);
	Position model = new Position();

	TabSheet tabSheet = new TabSheet();

	TextField name = new TextField("Position name");
	TextArea description = new TextArea("Description");
	TextField department = new TextField("Department");
	TextField location = new TextField("Location");
	ComboBox<String> employmentType = new ComboBox<>("Employment type");
	SalaryRangePicker salaryRange = new SalaryRangePicker("Salary range");

	FieldsToolbar toolbar = new FieldsToolbar();
	FormСreationComponent form = new FormСreationComponent();

	Button createPositionBtn = new Button("Create position");

	PositionService positionService;

	public PositionCreateView(PositionService positionService) {
		this.positionService = positionService;
		this.configureComponents();
		this.configureBinder();
		tabSheet.add("Position Info", this.getPositionInfoSheet());
		tabSheet.add("Application Form", this.getFormSheet());
		tabSheet.add("Complete", this.getCompleteSheet());
		tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);
		tabSheet.setSizeFull();
		add(tabSheet);

	}

	private void configureComponents() {
		UiUtils.setValueChangeMode(ValueChangeMode.EAGER, name, description, department, location);
		form.setWidthFull();
		employmentType.setItems(Arrays.stream(EmploymentType.values()).map(EmploymentType::toString).toList());
		employmentType.setItemLabelGenerator(i -> EmploymentType.valueOf(i).getLabel());

		createPositionBtn.addClickListener(e -> {
			var pos = this.positionService.createPosition(binder.getBean(), form);
			UI.getCurrent().navigate(PositionView.class, new RouteParam("posId", pos.getId()));
		});
	}

	private void configureBinder() {
		binder.bindInstanceFields(this);
		binder.addStatusChangeListener(e -> createPositionBtn.setEnabled(binder.isValid()));
		binder.forField(salaryRange).withNullRepresentation(new SalaryRange(0.0, 0.0, "USD"))
				.withValidator(
						decimalRange -> decimalRange.getStart() == null || decimalRange.getEnd() == null
								|| decimalRange.getStart() < decimalRange.getEnd(),
						"Min salary should be less or equal to max salary")
				.bind(position -> new SalaryRange(position.getMinSalary(), position.getMaxSalary(),
						position.getCurrencyCode()), (position, salaryRange) -> {
							position.setMinSalary(salaryRange.getStart());
							position.setMaxSalary(salaryRange.getEnd());
							position.setCurrencyCode(salaryRange.getCurrencyCode());
						});
		binder.setBean(model);
	}

	private Component getPositionInfoSheet() {
		VerticalLayout infoSheet = new VerticalLayout();
		infoSheet.setSizeFull();
		infoSheet.setAlignItems(Alignment.CENTER);
		infoSheet.add(name, description, department, location, employmentType, salaryRange);
		this.setWidth("40vw", name, description, department, location, employmentType, salaryRange);
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
		// formCanvas.setAlignItems(Alignment.CENTER);
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
		Span note = new Span(
				"First name, Last name and Date of birth are mandantory data and will be automatically created");
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
