package edu.chnu.recruiting.front.views.registration;

import java.util.stream.Stream;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;

import lombok.Getter;

public class SignUpForm extends VerticalLayout {
	private H2 title = new H2("Sign up");

	private Binder<SignUpModel> binder = new BeanValidationBinder<SignUpModel>(SignUpModel.class);

	private TextField username = new TextField("Username");
	private TextField email = new TextField("Email");
	private PasswordField password = new PasswordField("Password");
	private PasswordField confirmPassword = new PasswordField("Confirm password");
	private Checkbox asCompany = new Checkbox("Register as Recruiter");

	private Button confirmBtn = new Button("Sign up");
	private Button cancelBtn = new Button("Cancel");

	public SignUpForm() {
		this(new SignUpModel());
	}

	public SignUpForm(SignUpModel model) {
		binder.bindInstanceFields(this);

		setHeightFull();
		addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);

		configureComponents();

		configureBinder();
		binder.addStatusChangeListener(e -> confirmBtn.setEnabled(binder.isValid()));
		binder.setBean(model);

		HorizontalLayout hz = new HorizontalLayout(confirmBtn, cancelBtn);

		add(title, username, email, password, confirmPassword, asCompany, hz);
	}

	private void configureComponents() {
		title.addClassName(TextAlignment.LEFT);
		setRequiredIndicatorVisible(username, email, password, confirmPassword);
		setSize(title, username, email, password, confirmPassword);
		setEagerChangeMode(username, email, password, confirmPassword);

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

	private void setSize(HasSize... components) {
		Stream.of(components).forEach(comp -> comp.setWidth("315px"));
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
