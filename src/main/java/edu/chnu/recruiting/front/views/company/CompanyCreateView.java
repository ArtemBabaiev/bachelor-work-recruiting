package edu.chnu.recruiting.front.views.company;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.company.CompanyForm.SaveEvent;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.UnitOfWork;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Create Company")
@Route(value = "company-create", layout = MainLayout.class)
@RolesAllowed({ "USER" })
public class CompanyCreateView extends Div {
	private CompanyService companyService;
	private SecurityContext securityService;
	private CompanyForm form;

	public CompanyCreateView(UnitOfWork uow) {
		this.companyService = uow.getCompanyService();
		this.securityService = uow.getSecurityContext();
		this.form = new CompanyForm(new CompanyFormModel(),
				query -> companyService.provideUsersForForm(query.getFilter().orElse(""),
						PageRequest.of(query.getPage(), query.getLimit())).stream(),
				personSearchTerm -> personSearchTerm);
		setHeightFull();
		this.form.setHeightFull();
		this.form.addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);

		configureComponents();
	}

	private void configureComponents() {
		this.form.addSaveListener(e -> handleSaveClick(e));
		this.form.addCancelListener(e -> UI.getCurrent().getPage().getHistory().back());

		add(form);
	}

	private void handleSaveClick(SaveEvent e) {
		try {
			this.companyService.createCompany(e.getModel());
			securityService.logout();
		} catch (AlreadyExistsException ex) {
			Notification notification = Notification.show(ex.getMessage(), 5000, Position.BOTTOM_STRETCH);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
}
