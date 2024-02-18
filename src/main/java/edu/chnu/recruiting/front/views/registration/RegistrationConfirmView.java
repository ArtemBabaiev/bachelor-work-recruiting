package edu.chnu.recruiting.front.views.registration;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.exceptions.VerificationTokenExpiredException;
import edu.chnu.recruiting.front.components.layouts.MainLayout;
import edu.chnu.recruiting.services.UserService;
import edu.chnu.recruiting.services.VerificationTokenService;

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
		vLayout.addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);
		try {
			this.tokenService.confirmRegistration(token);	
			vLayout.add(message, homeBtn);
		} catch (VerificationTokenExpiredException e) {
			vLayout.add(message, homeBtn, resendBtn);
		}
		add(vLayout);
	}
	
	
	
	

}
