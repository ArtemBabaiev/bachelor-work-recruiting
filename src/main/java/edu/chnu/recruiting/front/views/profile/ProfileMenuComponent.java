package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.sidenav.SideNavItem;

import edu.chnu.recruiting.front.views.management.company.CompanyMgmtView;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.utils.enums.StarterRoles;

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
	
	public void showAdditionalItems(User user) {
		if (user.getRole().getName().equals(StarterRoles.COMPANY.getName())) {
			this.addItem(new SideNavItem("Company", CompanyMgmtView.class));			
		}
	}
}
