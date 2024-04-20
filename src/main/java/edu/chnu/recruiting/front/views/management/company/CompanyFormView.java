package edu.chnu.recruiting.front.views.management.company;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.exceptions.ForbiddenException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.management.company.CompanyForm.SaveEvent;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.AccessService;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.UnitOfWork;
import jakarta.annotation.security.PermitAll;

@PageTitle("Create Company")
@Route(value = "management/company/form", layout = MainLayout.class)
@PermitAll
public class CompanyFormView extends VerticalLayout implements BeforeEnterObserver{
	private SecurityContext securityContext;
	private CompanyService companyService;
	private AccessService accessService;
	
	private CompanyForm form;
	
	public CompanyFormView(UnitOfWork uow) {
		this.companyService = uow.getCompanyService();
		this.accessService = uow.getAccessService();
		this.securityContext = uow.getSecurityContext();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		CompanyFormModel model = this.companyService.getCompanyByAuthUser(CompanyFormModel.class);
		if (model != null && !this.accessService.canUserEditCompany(model)) {
			event.rerouteToError(ForbiddenException.class);
		}
		
		this.form = new CompanyForm(model == null? new CompanyFormModel(): model);
		
		initComponent();
	}
	
	private void initComponent() {
		this.form.addSaveListener(e -> handleSaveClick(e));
		this.form.addCancelListener(e -> UI.getCurrent().getPage().getHistory().back());
		
		setSizeFull();
		add(form);
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
