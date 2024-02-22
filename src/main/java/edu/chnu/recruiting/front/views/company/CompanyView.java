package edu.chnu.recruiting.front.views.company;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.services.CompanyService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Company")
@Route(value = "company",layout = MainLayout.class)
@RolesAllowed({"COMPANY"})
public class CompanyView extends Div{
	private CompanyService companyService;
	public CompanyView(CompanyService companyService) {
		this.companyService = companyService;
		add(new H2(companyService.getModelByCurrentUser().toString()));
	}
}
