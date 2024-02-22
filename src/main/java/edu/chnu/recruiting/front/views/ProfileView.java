package edu.chnu.recruiting.front.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.company.CompanyCreateView;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.UnitOfWork;
import edu.chnu.recruiting.services.UserService;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile",layout = MainLayout.class)
@PermitAll
public class ProfileView extends Div{

	private Button registerCompnanyBtn = new Button("Register company");
	
	private UserService userService;
	
	private CompanyService companyService;
	
	public ProfileView(UnitOfWork uow) {
		this.userService = uow.getUserService();
		this.companyService = uow.getCompanyService();
		configureComponents();
		add(registerCompnanyBtn);
	}

	private void configureComponents() {
		registerCompnanyBtn.addClickListener(e -> {
			UI.getCurrent().navigate(CompanyCreateView.class);
		});
		
	}
}
