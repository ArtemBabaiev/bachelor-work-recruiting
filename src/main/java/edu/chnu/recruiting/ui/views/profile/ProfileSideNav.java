package edu.chnu.recruiting.ui.views.profile;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.ui.views.management.company.CompanyMgmtView;
import edu.chnu.recruiting.utils.enums.StarterRoles;

public class ProfileSideNav extends SideNav {

	public ProfileSideNav(User user) {
		this.addItem(new SideNavItem("Account", AccountProfileView.class));
		this.addItem(new SideNavItem("Applications", ApplicationsProfileView.class));
		if (user.getRole().getName().equals(StarterRoles.COMPANY.getName())) {
			this.addItem(new SideNavItem("Company Management", CompanyMgmtView.class));
		} else if (user.getRole().getName().equals(StarterRoles.USER.getName())) {
			this.addItem(new SideNavItem("Create Company", CreateCompanyView.class));
		}
	}
}
