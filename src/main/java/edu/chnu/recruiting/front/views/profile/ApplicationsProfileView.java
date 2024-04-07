package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.data.ApplicationDataProvider;
import edu.chnu.recruiting.front.data.ApplicationProfileFilter;
import edu.chnu.recruiting.front.data.IFilter;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.ApplicationSummary;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.viewModels.ApplicationProfileGridVM;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.UnitOfWork;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.PermitAll;

@Route(value = "profile/applications", layout = MainLayout.class)
@PageTitle("Profile-Applications")
@PermitAll
public class ApplicationsProfileView extends VerticalLayout {
	private ApplicationService applicationService;
	private User loggedInUser;
	
	private Grid<ApplicationProfileGridVM> grid;
	private ApplicationDataProvider<ApplicationProfileGridVM> dataProvider;
	private ApplicationProfileFilter applicatinoFilter;
	private ConfigurableFilterDataProvider<ApplicationProfileGridVM, Void, IFilter<ApplicationSummary>> filterDataProvider;
	
	private TextField nameSearch = new TextField();
	private ComboBox<String> statusBox = new ComboBox<String>();
	private ProfileMenuComponent menuBar = new ProfileMenuComponent();

	public ApplicationsProfileView(UnitOfWork uow) {
		this.applicationService = uow.getApplicationService();
		this.loggedInUser = uow.getSecurityContext().getAuthenticatedUser();
		
		grid = new Grid<>(ApplicationProfileGridVM.class, false);
		dataProvider = new ApplicationDataProvider<ApplicationProfileGridVM>(this.applicationService, ApplicationProfileGridVM.class);
		applicatinoFilter = new ApplicationProfileFilter(this.loggedInUser);
		filterDataProvider = dataProvider.withConfigurableFilter();
		filterDataProvider.setFilter(applicatinoFilter);

		setSizeFull();

		configureGrid();
		configureComponents();

		add(menuBar, new HorizontalLayout(nameSearch, statusBox), grid);
	}

	private void configureGrid() {
		grid.addColumn(p -> p.getPositionName(), "positionName").setHeader("Position");
		grid.addColumn(p -> p.getSubmittedAt(), "submittedAt").setHeader("Submitted at");
		grid.addComponentColumn(p -> ApplicationStatus.getBadge(p.getStatus())).setHeader("Status");
		grid.addColumn(p -> p.getRejectReason()).setSortable(false).setHeader("Reject reason");
		grid.setItems(filterDataProvider);
	}

	private void configureComponents() {
		statusBox.setClearButtonVisible(true);
		statusBox.setItems(ApplicationStatus.getAllValues());
		statusBox.setItemLabelGenerator(s -> ApplicationStatus.getLabel(s));
		statusBox.addValueChangeListener(e -> {
			applicatinoFilter.setStatus(e.getValue());
			filterDataProvider.refreshAll();
		});
		statusBox.setPlaceholder("Status");

		nameSearch.setPlaceholder("Position title");
		nameSearch.setClearButtonVisible(true);
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			applicatinoFilter.setPositionName(e.getValue());
			filterDataProvider.refreshAll();
		});


		

	}
}
