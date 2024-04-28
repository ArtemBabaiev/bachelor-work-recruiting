package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.ServiceManager;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile/account", layout = MainLayout.class)
@PermitAll
public class AccountProfileView extends ProfileView {

	private SecurityContext securityContext;

	private User loggedInUser;

	public AccountProfileView(ServiceManager uow) {
		super(uow.getSecurityContext().getAuthenticatedUser());
		this.securityContext = uow.getSecurityContext();
		loggedInUser = this.securityContext.getAuthenticatedUser();
		setContent(getContent());
	}

	private Component getContent() {
		return new H2(loggedInUser.getUsername());
	}

}
