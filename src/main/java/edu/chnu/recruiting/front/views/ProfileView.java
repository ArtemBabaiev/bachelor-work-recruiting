package edu.chnu.recruiting.front.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.company.CompanyCreateView;
import edu.chnu.recruiting.front.views.company.CompanyView;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.services.UnitOfWork;
import edu.chnu.recruiting.services.UserService;
import edu.chnu.recruiting.utils.constants.StarterRoles;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile",layout = MainLayout.class)
@PermitAll
public class ProfileView extends Div{

	private Button registerCompnanyBtn = new Button("Register company");
	private Button viewCompnanyBtn = new Button("View company");
	
	private UserService userService;
	
	private User loggedInUser;
	
	public ProfileView(UnitOfWork uow) {
		this.userService = uow.getUserService();
		loggedInUser = this.userService.getAuthenticatedUser();
		configureComponents();
		if (!loggedInUser.getRole().getName().equals(StarterRoles.USER.getName())) {
			add(viewCompnanyBtn);			
		} else {
			add(registerCompnanyBtn);
		}
	}

	private void configureComponents() {
		registerCompnanyBtn.addClickListener(e -> {
			UI.getCurrent().navigate(CompanyCreateView.class);
		});
		
		viewCompnanyBtn.addClickListener(e -> {
			UI.getCurrent().navigate(CompanyView.class);
		});
		
	}
}
