package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ProfileMenuComponent extends MenuBar {
	MenuItem account;
	MenuItem applications;

	public ProfileMenuComponent() {
		super();
		initComponent();
	}

	private void initComponent() {
		this.addThemeVariants(MenuBarVariant.LUMO_SMALL, MenuBarVariant.LUMO_TERTIARY);
		
		account = this.addItem(new SideNavItem("Account", ProfileView.class));
		applications = this.addItem(new SideNavItem("Applications", ApplicationsProfileView.class));
	}
}
