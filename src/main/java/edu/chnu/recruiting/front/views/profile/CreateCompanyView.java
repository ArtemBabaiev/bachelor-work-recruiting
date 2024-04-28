package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.management.company.CompanyForm;
import edu.chnu.recruiting.front.views.management.company.CompanyForm.SaveEvent;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.ServiceManager;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Create Company")
@Route(value = "profile/create-company", layout = MainLayout.class)
@RolesAllowed({ "USER" })
public class CreateCompanyView extends ProfileView implements BeforeEnterObserver {
	private SecurityContext securityContext;
	private CompanyService companyService;

	private CompanyForm form;

	public CreateCompanyView(ServiceManager uow) {
		super(uow.getSecurityContext().getAuthenticatedUser());
		this.companyService = uow.getCompanyService();
		this.securityContext = uow.getSecurityContext();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {

		this.form = new CompanyForm(new CompanyFormModel());

		initComponent();
	}

	private void initComponent() {
		this.form.addSaveListener(e -> handleSaveClick(e));
		this.form.addCancelListener(e -> UI.getCurrent().getPage().getHistory().back());

		setSizeFull();
		setContent(form);
	}

	private void handleSaveClick(SaveEvent e) {
		try {
			if (e.getModel().getId() == null) {
				this.companyService.createCompany(e.getModel());
				securityContext.logout();
			} else {
				this.companyService.updateCompany(e.getModel());
			}
		} catch (AlreadyExistsException ex) {
			Notification notification = Notification.show(ex.getMessage(), 5000, Position.BOTTOM_STRETCH);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
}