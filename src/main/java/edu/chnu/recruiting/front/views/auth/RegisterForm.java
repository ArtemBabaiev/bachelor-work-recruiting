package edu.chnu.recruiting.front.views.auth;

import java.util.stream.Stream;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;

public class RegisterForm extends FormLayout {
	private H2 title = new H2("Sign up");
	
	private Binder<RegisterModel> binder = new BeanValidationBinder<RegisterModel>(RegisterModel.class);
	
	private TextField username = new TextField("Username");
	private TextField email = new TextField("Email");
	private PasswordField password = new PasswordField("Password");
	private PasswordField confirmPassword = new PasswordField("Confirm password");
	private Checkbox asCompany = new Checkbox("Register as Recruiter");

	private Button confirmBtn = new Button("Sign in");
	private Button cancelBtn = new Button("Cancel");

	public RegisterForm() {
		this(new RegisterModel());
	}

	public RegisterForm(RegisterModel model) {
		binder.bindInstanceFields(this);
		
		setHeightFull();
		addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);
		
		title.addClassName(TextAlignment.LEFT);
		setRequiredIndicatorVisible(username, email, password, confirmPassword);
		setSize(title, username, email, password, confirmPassword);
		setEagerChangeMode(username, email, password, confirmPassword);
		
		binder.addStatusChangeListener(e -> confirmBtn.setEnabled(binder.isValid()));
		binder.setBean(model);
		
		add(title, username, email, password, confirmPassword, asCompany, confirmBtn, cancelBtn);
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
}
