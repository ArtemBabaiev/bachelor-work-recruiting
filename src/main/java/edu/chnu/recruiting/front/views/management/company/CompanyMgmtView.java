package edu.chnu.recruiting.front.views.management.company;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.models.formModels.RecruiterFormModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.ServiceManager;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Company")
@Route(value = "management/company", layout = MainLayout.class)
@RolesAllowed({ "COMPANY" })
public class CompanyMgmtView extends SplitLayout {

	private Grid<User> grid = new Grid<>(User.class, false);
	private RecruiterForm form;
	private CompanyForm companyForm;

	private SecurityContext securityContext;
	private CompanyService companyService;

	private CompanyFormModel company;

	public CompanyMgmtView(ServiceManager uow) {
		this.companyService = uow.getCompanyService();
		this.securityContext = uow.getSecurityContext();
		company = companyService.getCompanyByAuthUser(CompanyFormModel.class);

		initComponent();
	}

	public void initComponent() {
		addClassName("list-view");
		setSizeFull();
		configureRecruiterGrid();
		configureRecruiterForm();
		configureCompanyForm();
		addToSecondary(new VerticalLayout(getRecruiterToolbar(), getRecruiterContent()));
		addToPrimary(getCompanyContent());
		setSplitterPosition(40);
		updateList();
		closeEditor();
	}

	private void configureCompanyForm() {
		this.companyForm = new CompanyForm(company);
		this.companyForm.addSaveListener(e -> handleSaveCompany(e));
		this.companyForm.addCancelListener(e -> handleCancelClick(e));
	}

	private Component getCompanyContent() {
		companyForm.setSizeFull();
		return companyForm;
	}

	private HorizontalLayout getRecruiterContent() {
		HorizontalLayout content = new HorizontalLayout(grid, form);
		content.setFlexGrow(2, grid);
		content.setFlexGrow(1, form);
		content.addClassNames("content");
		content.setSizeFull();
		return content;
	}

	private void configureRecruiterForm() {
		form = new RecruiterForm();
		form.setWidth("25em");
		form.addSaveListener(this::saveRecruiter);
		form.addDeleteListener(this::deleteRecruiter);
		form.addCloseListener(e -> closeEditor());
	}

	private void configureRecruiterGrid() {
		grid.addClassNames("user-grid");
		grid.setSizeFull();

		grid.addColumn(User::getUsername).setHeader("Username");
		grid.addColumn(u -> u.getRole().getName().replace("ROLE_", "")).setHeader("Role");
		grid.addComponentColumn(u -> getEnabledBadge(u.isEnabled())).setHeader("Status");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));

		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				editUser(RecruiterFormModel.of(event.getValue()));
			}
		});
	}

	private Component getRecruiterToolbar() {
		Button addContactButton = new Button("Add contact");
		addContactButton.addClickListener(click -> addUser());

		var toolbar = new HorizontalLayout(addContactButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private Component getEnabledBadge(boolean enabled) {
		Span badge = new Span();
		Icon icon;
		Span text;
		if (enabled) {
			icon = VaadinIcon.CHECK.create();
			text = new Span("Enabled");
			badge.getElement().getThemeList().add("badge success");
		} else {
			icon = VaadinIcon.CLOSE.create();
			text = new Span("Disabled");
			badge.getElement().getThemeList().add("badge error");
		}
		icon.getStyle().set("padding", "var(--lumo-space-xs)");
		badge.add(icon, text);

		return badge;
	}

	public void editUser(RecruiterFormModel model) {
		if (model == null) {
			closeEditor();
		} else {
			form.setBean(model);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void closeEditor() {
		form.setBean(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	private void addUser() {
		grid.asSingleSelect().clear();
		editUser(new RecruiterFormModel());
	}

	private void updateList() {
		grid.setItems(companyService.getCompanyRecruiters(company.getId()));
	}

	private void saveRecruiter(RecruiterForm.SaveEvent event) {
		try {
			companyService.saveRecruiter(company.getId(), event.getModel());
			updateList();
			closeEditor();
		} catch (AlreadyExistsException e) {
			Notification notif = Notification.show(e.getMessage(), 5000, Position.BOTTOM_CENTER);
			notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	private void deleteRecruiter(RecruiterForm.DeleteEvent event) {
		companyService.deleteRecruiter(company.getId(), event.getModel());
		updateList();
		closeEditor();
	}

	private void handleSaveCompany(CompanyForm.SaveEvent e) {
		try {
			if (e.getModel().getId() == null) {
				this.companyService.createCompany(e.getModel());
				securityContext.logout();
			} else {
				this.companyService.updateCompany(e.getModel());
				UI.getCurrent().getPage().reload();
			}
		} catch (AlreadyExistsException ex) {
			Notification.show(ex.getMessage(), 5000, Position.BOTTOM_STRETCH).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void handleCancelClick(CompanyForm.CancelEvent e) {
		this.company = companyService.getCompanyByAuthUser(CompanyFormModel.class);
		companyForm.setBean(company);
	}

}
