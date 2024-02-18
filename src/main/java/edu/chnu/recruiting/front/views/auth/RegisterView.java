package edu.chnu.recruiting.front.views.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.HomeView;
import edu.chnu.recruiting.front.views.auth.RegisterForm.CancelEvent;
import edu.chnu.recruiting.front.views.auth.RegisterForm.SaveEvent;
import edu.chnu.recruiting.services.UserService;

@Route("register") 
@PageTitle("Sign Up")
@AnonymousAllowed
public class RegisterView extends Div{
	
	private UserService userService;
	
	private RegisterForm form;
	
	public RegisterView(UserService userService) {
		this.userService = userService;
		setSizeFull();
		initComponents();
		configureComponents();
		
		add(form);
	}

	private void initComponents() {
		form = new RegisterForm();
	}
	
	private void configureComponents() {
		form.addSaveListener(e -> handleSaveEvent(e));
		form.addCancelListener(e -> handleCancelEvent(e));
	}

	private void handleCancelEvent(CancelEvent e) {
		History history = UI.getCurrent().getPage().getHistory();
		history.back();
		return;
	}

	private void handleSaveEvent(SaveEvent e) {
		try {
			this.userService.registerUser(e.getModel());
			UI.getCurrent().navigate(HomeView.class);
		} catch (AlreadyExistsException ex) {
			Notification notification = Notification.show(ex.getMessage(), 5000, Position.BOTTOM_STRETCH);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}


}
