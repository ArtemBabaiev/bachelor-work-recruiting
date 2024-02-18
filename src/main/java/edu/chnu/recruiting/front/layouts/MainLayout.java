package edu.chnu.recruiting.front.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.views.LoginView;
import edu.chnu.recruiting.front.views.registration.RegistrationConfirmView;
import edu.chnu.recruiting.front.views.registration.SignUpView;

public class MainLayout extends AppLayout { 

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE, 
            LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo ); 

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); 
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header); 

    }

    private void createDrawer() {
    	setDrawerOpened(false);
    	VerticalLayout vLayout = new VerticalLayout();
    	SideNav nav = new SideNav();
    	nav.addItem(
    			new SideNavItem("Login", LoginView.class),
    			new SideNavItem("Sign-up", SignUpView.class),
    			new SideNavItem("Confirmation", RegistrationConfirmView.class)
    			);

    	var themeToggle = new Checkbox("Dark theme");
    	 themeToggle.addValueChangeListener(e -> {
             setTheme(e.getValue());
         });
    	
    	vLayout.add(nav, themeToggle);
        addToDrawer(vLayout);
    }
    
    private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";

        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }
}
