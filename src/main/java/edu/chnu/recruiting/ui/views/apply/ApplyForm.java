package edu.chnu.recruiting.ui.views.apply;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.models.formModels.ApplyFormModel;
import lombok.Getter;

public class ApplyForm extends FormLayout {
	private Binder<ApplyFormModel> binder = new BeanValidationBinder<ApplyFormModel>(ApplyFormModel.class);

	private EmailField email = new EmailField("Email");
	private TextField fullName = new TextField("Full name");
	private DatePicker dateOfBirth = new DatePicker("Date of birth");

	private Button continueBtn = new Button("Continue");
	private Button cancelBtn = new Button("Cancel");

	public ApplyForm(ApplyFormModel model) {
		DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
		multiFormatI18n.setDateFormats("dd.MM.yyyy", "MM/dd/yyyy");
		dateOfBirth.setI18n(multiFormatI18n);

		cancelBtn.addClickListener(e -> fireEvent(new CancelEvent(this)));
		continueBtn.addClickListener(e -> fireEvent(new ContinueEvent(this, binder.getBean())));

		binder.bindInstanceFields(this);
		binder.addStatusChangeListener(e -> continueBtn.setEnabled(binder.isValid()));
		binder.setBean(model);

		HorizontalLayout controls = new HorizontalLayout(cancelBtn, continueBtn);

		add(email, fullName, dateOfBirth, controls);

		this.setColspan(controls, 2);
	}
	
	public Registration addContinueListener(ComponentEventListener<ContinueEvent> listener) {
		return addListener(ContinueEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	@Getter
	public static abstract class ApplyFormEvent extends ComponentEvent<ApplyForm> {
		private ApplyFormModel model;

		protected ApplyFormEvent(ApplyForm source, ApplyFormModel model) {
			super(source, false);
			this.model = model;
		}
	}

	public static class ContinueEvent extends ApplyFormEvent {

		ContinueEvent(ApplyForm source, ApplyFormModel model) {
			super(source, model);
		}
	}

	public static class CancelEvent extends ApplyFormEvent {

		CancelEvent(ApplyForm source) {
			super(source, null);
		}
	}
}
