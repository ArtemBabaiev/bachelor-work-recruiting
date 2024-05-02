package edu.chnu.recruiting.front.views.auth.registration;

import java.util.stream.Stream;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

public class SignUpForm extends FormLayout {

	private Binder<SignUpModel> binder = new BeanValidationBinder<SignUpModel>(SignUpModel.class);

	private TextField username = new TextField("Username");
	private TextField email = new TextField("Email");
	private TextField fullName = new TextField("Full name");
	private DatePicker dateOfBirth = new DatePicker("Date of birth");
	private PasswordField password = new PasswordField("Password");
	private PasswordField confirmPassword = new PasswordField("Confirm password");

	private Button confirmBtn = new Button("Sign up");
	private Button cancelBtn = new Button("Cancel");

	public SignUpForm() {
		this(new SignUpModel());
	}

	public SignUpForm(SignUpModel model) {
		binder.bindInstanceFields(this);

		configureComponents();

		configureBinder();
		binder.addStatusChangeListener(e -> confirmBtn.setEnabled(binder.isValid()));
		binder.setBean(model);

		HorizontalLayout hz = new HorizontalLayout(confirmBtn, cancelBtn);

		add(username, email, fullName, dateOfBirth, password, confirmPassword, hz);
	}

	private void configureComponents() {
		setRequiredIndicatorVisible(username, email, password, confirmPassword);
		setEagerChangeMode(username, email, password, confirmPassword);

		DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
		multiFormatI18n.setDateFormats("dd.MM.yyyy", "MM/dd/yyyy");
		dateOfBirth.setI18n(multiFormatI18n);

		confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

		confirmBtn.addClickListener(e -> handleSaveClick(e));
		cancelBtn.addClickListener(e -> fireEvent(new CancelEvent(this)));
	}

	private void configureBinder() {
		binder.forField(confirmPassword).withValidator(e -> {
			return e.equals(password.getValue());
		}, "Passwords do not match").bind("confirmPassword");
	}

	private void handleSaveClick(ClickEvent<Button> e) {
		if (binder.isValid())
			fireEvent(new SaveEvent(this, binder.getBean()));
	}

	private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
		Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
	}

	private void setEagerChangeMode(HasValueChangeMode... components) {
		Stream.of(components).forEach(comp -> comp.setValueChangeMode(ValueChangeMode.EAGER));
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	@Getter
	public static abstract class RegisterFormEvent extends ComponentEvent<SignUpForm> {
		private SignUpModel model;

		protected RegisterFormEvent(SignUpForm source, SignUpModel model) {
			super(source, false);
			this.model = model;
		}
	}

	public static class SaveEvent extends RegisterFormEvent {

		SaveEvent(SignUpForm source, SignUpModel model) {
			super(source, model);
		}
	}

	public static class CancelEvent extends RegisterFormEvent {

		CancelEvent(SignUpForm source) {
			super(source, null);
		}
	}
}
