package edu.chnu.recruiting.front.views.company;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.viewModels.CompanyViewModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.services.CompanyService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Company")
@Route(value = "company",layout = MainLayout.class)
@RolesAllowed({"COMPANY"})
public class CompanyView extends Div{
	private Grid<User> grid = new Grid<>(User.class, false);
	private H2 title = new H2();
	
	private CompanyService companyService;
	private CompanyViewModel model;
	public CompanyView(CompanyService companyService) {
		this.companyService = companyService;
		this.model = this.companyService.getCompanyVMByAuthUser();
		configureComponents();
		configureGrid();
		updateGrid();
		add(title, grid);
	}
	private void configureComponents() {
		title.setText(model.getName());
		
	}
	private void configureGrid() {
		grid.addColumn(User::getEmail).setHeader("Email");
		grid.addColumn(User::getUsername).setHeader("Username");
		grid.addColumn(u -> u.getRole().getName().replace("ROLE_", "")).setHeader("Role");
		
	}
	
	private void updateGrid() {
		var users = model.getRecruiters();
		users.add(model.getOwner());
		grid.setItems(users);
	}
}
