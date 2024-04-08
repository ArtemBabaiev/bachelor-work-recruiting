package edu.chnu.recruiting.front.layouts;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.views.auth.LoginView;
import edu.chnu.recruiting.front.views.auth.registration.SignUpView;
import edu.chnu.recruiting.front.views.management.application.ApplicationsMgmtView;
import edu.chnu.recruiting.front.views.management.company.CompanyMgmtView;
import edu.chnu.recruiting.front.views.management.position.PositionsMgmtView;
import edu.chnu.recruiting.front.views.position.PositionListingView;
import edu.chnu.recruiting.front.views.profile.ProfileView;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.utils.PropertiesReader;
import edu.chnu.recruiting.utils.enums.StarterRoles;

public class MainLayout extends AppLayout {

	private SecurityContext securityService;
	private PropertiesReader propertiesReader;

	private H2 title;

	public MainLayout(SecurityContext securityService, PropertiesReader propertiesReader) {
		this.securityService = securityService;
		this.propertiesReader = propertiesReader;
		setPrimarySection(Section.DRAWER);
		addDrawerContent();
		addHeaderContent();
		setDrawerOpened(false);
	}

	private void addHeaderContent() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.setAriaLabel("Menu toggle");

		title = new H2(this.propertiesReader.getApplicationName());
		title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

		Button authBtn = new Button();
		HorizontalLayout header = new HorizontalLayout(title);
		header.setAlignItems(Alignment.CENTER);
		header.addClassNames(LumoUtility.Margin.Right.MEDIUM);
		header.setWidthFull();
		header.expand(title);
		if (this.securityService.isAuthenticated()) {
			authBtn.setText("Log out");
			authBtn.addClickListener(e -> this.securityService.logout());
		} else {
			authBtn.setText("Log in");
			authBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			authBtn.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
			var signUpBtn = new Button("Sign Up", e -> UI.getCurrent().navigate(SignUpView.class));
			signUpBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			header.add(signUpBtn);
		}
		header.add(authBtn);

		addToNavbar(true, toggle, header);
	}

	private void addDrawerContent() {
		H1 appName = new H1("My App");
		appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
		Header header = new Header(appName);

		Scroller scroller = new Scroller(createNavigation());

		addToDrawer(header, scroller, createFooter());
	}

	private SideNav createNavigation() {
		SideNav nav = new SideNav();

		nav.addItem(new SideNavItem("Profile", ProfileView.class));
		nav.addItem(new SideNavItem("Positions listing", PositionListingView.class));
		var user = this.securityService.getAuthenticatedUserSilent();
		if (user != null) {
			String role = user.getRole().getName();
			if (role.equals(StarterRoles.COMPANY.getName())) {
				this.setOwnerNavigation(nav);
			} else if (role.equals(StarterRoles.RECRUITER.getName())) {
				this.setRecruiterNavigation(nav);
			}
		}

		return nav;
	}

	private void setRecruiterNavigation(SideNav nav) {
		nav.addItem(new SideNavItem("Positions Managment", PositionsMgmtView.class));
		nav.addItem(new SideNavItem("Applications Managment", ApplicationsMgmtView.class));
	}

	private void setOwnerNavigation(SideNav nav) {
		nav.addItem(new SideNavItem("Company Managment", CompanyMgmtView.class));
		nav.addItem(new SideNavItem("Positions Managment", PositionsMgmtView.class));
		nav.addItem(new SideNavItem("Applications Managment", ApplicationsMgmtView.class));
	}

	private Footer createFooter() {
		Footer layout = new Footer();

		return layout;
	}
}
