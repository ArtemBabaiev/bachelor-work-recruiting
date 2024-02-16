package edu.chnu.recruiting.front.views.auth;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.views.auth.RegisterForm.CancelEvent;
import edu.chnu.recruiting.front.views.auth.RegisterForm.SaveEvent;

@Route("register") 
@PageTitle("Sign Up")
@AnonymousAllowed
public class RegisterView extends Div{
	
	RegisterForm form;
	
	public RegisterView() {
		setSizeFull();
		initComponents();
		configureComponents();
	}

	private void initComponents() {
		form = new RegisterForm();
		add(form);
	}
	
	private void configureComponents() {
		form.addSaveListener(e -> handleSaveEvent(e));
		form.addCancelListener(e -> handleCancelEvent(e));
	}

	private void handleCancelEvent(CancelEvent e) {
		Notification.show("Canceled");
		return;
	}

	private void handleSaveEvent(SaveEvent e) {
		Notification.show("Saved " + e.getModel());
		return;
	}


}
