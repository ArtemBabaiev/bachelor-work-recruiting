package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.UnitOfWork;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile/account", layout = MainLayout.class)
@PermitAll
public class ProfileView extends VerticalLayout {

	private ProfileMenuComponent menuBar = new ProfileMenuComponent();

	private SecurityContext securityContext;

	private User loggedInUser;

	public ProfileView(UnitOfWork uow) {
		this.securityContext = uow.getSecurityContext();
		loggedInUser = this.securityContext.getAuthenticatedUser();
		add(menuBar, new H2(loggedInUser.getUsername()));
	}

}
