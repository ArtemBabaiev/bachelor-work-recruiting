package edu.chnu.recruiting.ui.views.management.position.components;

import java.util.Arrays;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.ui.components.fields.picker.SalaryRange;
import edu.chnu.recruiting.ui.components.fields.picker.SalaryRangePicker;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import lombok.Getter;

public class PositionForm extends FormLayout {
	private Binder<Position> binder = new BeanValidationBinder<Position>(Position.class);

	private TextField name = new TextField("Position name");
	private TextArea description = new TextArea("Description");
	private TextField department = new TextField("Department");
	private TextField location = new TextField("Location");
	private ComboBox<String> employmentType = new ComboBox<>("Employment type");
	private SalaryRangePicker salaryRange = new SalaryRangePicker("Salary range");

	private Button saveBtn = new Button("Save", e -> {
		if (binder.validate().isOk()) {
			fireEvent(new SaveEvent(this, binder.getBean()));
		}
	});
	
	private Button cancelBtn = new Button("Cancel", e -> fireEvent(new CancelEvent(this)));

	public PositionForm(Position bean) {
		configureBinder();
		configureComponents();
		add(name, description, department, location, employmentType, salaryRange);
		saveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		saveBtn.addClickShortcut(Key.ENTER);
		cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		add(new HorizontalLayout(saveBtn, cancelBtn));
		binder.setBean(bean);
	}

	private void configureComponents() {
		employmentType.setItems(Arrays.stream(EmploymentType.values()).map(EmploymentType::toString).toList());
		employmentType.setItemLabelGenerator(i -> EmploymentType.valueOf(i).getLabel());
	}

	private void configureBinder() {
		binder.bindInstanceFields(this);

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
	}
	
	public void setBean(Position model) {
		this.binder.setBean(model);
	}
	
	public Button getSaveButton() {
		return saveBtn;
	}
	
	public Button getCancelButton() {
		return cancelBtn;
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	@Getter
	public static abstract class PositionFormEvent extends ComponentEvent<PositionForm> {
		private Position model;

		protected PositionFormEvent(PositionForm source, Position model) {
			super(source, false);
			this.model = model;
		}
	}

	public static class SaveEvent extends PositionFormEvent {

		SaveEvent(PositionForm source, Position model) {
			super(source, model);
		}
	}

	public static class CancelEvent extends PositionFormEvent {

		CancelEvent(PositionForm source) {
			super(source, null);
		}
	}
}
