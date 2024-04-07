package edu.chnu.recruiting.front.layouts;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.views.auth.LoginView;
import edu.chnu.recruiting.front.views.auth.registration.SignUpView;
import edu.chnu.recruiting.front.views.management.company.CompanyView;
import edu.chnu.recruiting.front.views.management.position.PositionCreateView;
import edu.chnu.recruiting.front.views.position.PositionListingView;
import edu.chnu.recruiting.front.views.profile.ProfileView;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.utils.PropertiesReader;

public class MainLayout extends AppLayout { 

	private SecurityContext securityService;
	private PropertiesReader propertiesReader;
	
    public MainLayout(SecurityContext securityService, PropertiesReader propertiesReader) {
    	this.securityService = securityService;
    	this.propertiesReader = propertiesReader;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1(propertiesReader.getApplicationName());
        Button authBtn = new Button();
        authBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //authBtn.addClassName(Margin.MEDIUM);
        logo.addClassNames(
            LumoUtility.FontSize.LARGE);
        

        var header = new HorizontalLayout(new DrawerToggle(), logo, authBtn); 
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); 
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        if (this.securityService.isAuthenticated()) {
			authBtn.setText("Log out");
			authBtn.addClickListener(e -> this.securityService.logout());
		} else {
			authBtn.setText("Log in");
			authBtn.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
			header.add(new Button("Sign Up", e -> UI.getCurrent().navigate(SignUpView.class)));
		}
        addToNavbar(header); 

    }

    private void createDrawer() {
    	setDrawerOpened(false);
    	VerticalLayout vLayout = new VerticalLayout();
    	SideNav nav = new SideNav();
    	nav.addItem(
    			new SideNavItem("Profile", ProfileView.class),
    			new SideNavItem("Positions listing", PositionListingView.class),
    			new SideNavItem("Company", CompanyView.class),
    			new SideNavItem("Position-create", PositionCreateView.class)
    			);

    	vLayout.add(nav);
        addToDrawer(vLayout);
    }
}
