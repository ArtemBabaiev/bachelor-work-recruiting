package edu.chnu.recruiting.front.views.company;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.company.CompanyForm.SaveEvent;
import edu.chnu.recruiting.services.CompanyService;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Edit Company")
@Route(value = "company-edit", layout = MainLayout.class)
@RolesAllowed({ "COMPANY" })
public class CompanyEditView extends Div {
	private CompanyService companyService;
	private CompanyForm form;

	public CompanyEditView(CompanyService companyService) {
		this.companyService = companyService;
		this.form = new CompanyForm(this.companyService.getCompanyFMByAuthUser(),
				query -> this.companyService.provideUsersForForm(query.getFilter().orElse(""),
						PageRequest.of(query.getPage(), query.getLimit())).stream(),
				personSearchTerm -> personSearchTerm);
		configureComponents();
	}

	private void configureComponents() {
		this.form.addSaveListener(e -> handleSaveClick(e));
		this.form.addSaveListener(e -> UI.getCurrent().getPage().getHistory().back());
		add(form);
	}

	private void handleSaveClick(SaveEvent e) {
		Notification.show("Clicked Edit");
	}
}
