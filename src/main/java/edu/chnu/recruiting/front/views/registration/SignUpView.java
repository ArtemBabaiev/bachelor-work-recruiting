package edu.chnu.recruiting.front.views.registration;

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
import edu.chnu.recruiting.front.views.registration.SignUpForm.CancelEvent;
import edu.chnu.recruiting.front.views.registration.SignUpForm.SaveEvent;
import edu.chnu.recruiting.services.UserService;

@Route("sign-up") 
@PageTitle("Sign Up")
@AnonymousAllowed
public class SignUpView extends Div{
	
	private UserService userService;
	
	private SignUpForm form;
	
	public SignUpView(UserService userService) {
		this.userService = userService;
		setSizeFull();
		initComponents();
		configureComponents();
		
		add(form);
	}

	private void initComponents() {
		form = new SignUpForm();
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
