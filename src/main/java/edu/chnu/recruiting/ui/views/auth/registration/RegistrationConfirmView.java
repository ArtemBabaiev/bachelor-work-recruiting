package edu.chnu.recruiting.ui.views.auth.registration;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.exceptions.TokenInvalidException;
import edu.chnu.recruiting.exceptions.VerificationTokenExpiredException;
import edu.chnu.recruiting.services.VerificationTokenService;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.HomeView;

@Route(value = "registration-confirm", layout = MainLayout.class)
@PageTitle("Confirm")
@AnonymousAllowed
public class RegistrationConfirmView extends Div implements BeforeEnterObserver {

	private H2 message = new H2();

	private Button homeBtn = new Button("Back to home");
	private Button resendBtn = new Button("Resend confirmation");

	private String token;

	private VerificationTokenService tokenService;

	public RegistrationConfirmView(VerificationTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		QueryParameters queryParameters = event.getLocation().getQueryParameters();
		List<String> tokens = queryParameters.getParameters("token");
		if (!tokens.isEmpty()) {
			token = tokens.get(0);
		}
		initComponent();
	}

	private void initComponent() {
		setSizeFull();
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setHeightFull();
		vLayout.addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);
		resendBtn.addClickListener(e -> handleResendClick(e));
		homeBtn.addClickListener(e -> UI.getCurrent().navigate(HomeView.class));

		try {
			this.tokenService.confirmRegistration(token);
			message.setText("Verification Successful");
			vLayout.add(message, homeBtn);
		} catch (VerificationTokenExpiredException e) {
			message.setText("Verification link is expired");
			vLayout.add(message, homeBtn, resendBtn);
		} catch (TokenInvalidException e) {
			message.setText("Invalid verification");
			vLayout.add(message, homeBtn);
		}
		add(vLayout);
	}

	private Object handleResendClick(ClickEvent<Button> e) {
		this.tokenService.generateAndSendNewVerificationToken(token);
		Notification.show("Email was sent", 5000, Position.BOTTOM_CENTER);
		return null;
	}

}
