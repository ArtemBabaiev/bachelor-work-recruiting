package edu.chnu.recruiting.front.views.auth;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginI18n.ErrorMessage;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private LoginI18n i18n = LoginI18n.createDefault();
	private final LoginForm login = new LoginForm(i18n);
	private ErrorMessage errorMessage = new ErrorMessage();

	public LoginView() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		configureForm();
		
		add(login);
	}

	private void configureForm() {
		errorMessage.setTitle("Attempt failed");
		errorMessage.setMessage("Check your credentials and account verification");
		i18n.setErrorMessage(errorMessage);
		login.setI18n(i18n);
		login.setAction("login");
		login.setForgotPasswordButtonVisible(false);
		
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
			login.setError(true);
		}
	}
}