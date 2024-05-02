package edu.chnu.recruiting.front.views.auth.registration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.HomeView;
import edu.chnu.recruiting.front.views.auth.registration.SignUpForm.CancelEvent;
import edu.chnu.recruiting.front.views.auth.registration.SignUpForm.SaveEvent;
import edu.chnu.recruiting.services.UserService;

@Route("sign-up")
@PageTitle("Sign Up")
@AnonymousAllowed
public class SignUpView extends VerticalLayout {
	private H2 title = new H2("Sign up");

	private UserService userService;

	private SignUpForm form;

	public SignUpView(UserService userService) {
		this.userService = userService;
		initComponents();
		configureComponents();
		title.setWidthFull();
		title.addClassName(LumoUtility.TextAlignment.LEFT);
		Div test = new Div(title, form);
		test.setMaxWidth("850px");
		test.addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.CENTER, AlignItems.CENTER);
		this.setJustifyContentMode(JustifyContentMode.CENTER);
		this.setAlignItems(Alignment.CENTER);
		setSizeFull();
		add(test);
	}

	private void initComponents() {
		form = new SignUpForm();
	}

	private void configureComponents() {
		// form.setResponsiveSteps(new ResponsiveStep("0", 1));
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
