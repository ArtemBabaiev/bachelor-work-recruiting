package edu.chnu.recruiting.front.views.auth;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register") 
@PageTitle("Sign Up")
@AnonymousAllowed
public class RegisterView extends Div{
	
	RegisterForm form;
	
	public RegisterView() {
		setSizeFull();
		initComponents();
	}

	private void initComponents() {
		form = new RegisterForm();
		add(form);
		
	}
}
