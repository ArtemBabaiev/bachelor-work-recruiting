package edu.chnu.recruiting.front.views.management.company;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.viewModels.CompanyViewModel;
import edu.chnu.recruiting.services.CompanyService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Company")
@Route(value = "management/company",layout = MainLayout.class)
@RolesAllowed({"COMPANY"})
public class CompanyMgmtView extends VerticalLayout {
	private Grid<User> grid = new Grid<>(User.class, false);
	private H2 title = new H2();
	private Button editBtn = new Button("Edit company info", e -> UI.getCurrent().navigate(CompanyFormView.class));
	
	private CompanyService companyService;
	private CompanyViewModel model;
	public CompanyMgmtView(CompanyService companyService) {
		this.companyService = companyService;
		this.model = this.companyService.getCompanyVMByAuthUser();
		configureComponents();
		configureGrid();
		updateGrid();
		add(editBtn, title, grid);
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
