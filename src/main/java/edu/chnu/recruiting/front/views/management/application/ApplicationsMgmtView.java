package edu.chnu.recruiting.front.views.management.application;

import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import edu.chnu.recruiting.front.data.ApplicationDataProvider;
import edu.chnu.recruiting.front.data.ApplicationMgmtFilter;
import edu.chnu.recruiting.front.data.IFilter;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.ApplicationSummary;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.ApplicationMgmtGridVM;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Applications")
@Route(value = "management/applications", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class ApplicationsMgmtView extends VerticalLayout implements BeforeEnterObserver{
	private PositionService positionService;
	private ApplicationService applicationService;

	private Grid<ApplicationMgmtGridVM> grid;
	private ApplicationDataProvider<ApplicationMgmtGridVM> dataProvider;
	private ApplicationMgmtFilter applicatinoFilter = new ApplicationMgmtFilter();
	private ConfigurableFilterDataProvider<ApplicationMgmtGridVM, Void, IFilter<ApplicationSummary>> filterDataProvider;
	
	private TextField nameSearch = new TextField();
	private ComboBox<Position> positionsBox = new ComboBox<Position>();
	private ComboBox<String> statusBox = new ComboBox<String>();

	private Long qPositionId = null;
	
	public ApplicationsMgmtView(ServiceManager uow) {
		this.applicationService = uow.getApplicationService();
		this.positionService = uow.getPositionService();

		grid = new Grid<>(ApplicationMgmtGridVM.class, false);
		dataProvider = new ApplicationDataProvider<ApplicationMgmtGridVM>(this.applicationService, ApplicationMgmtGridVM.class);
		filterDataProvider = dataProvider.withConfigurableFilter();
		filterDataProvider.setFilter(applicatinoFilter);

		
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		
		try {
			qPositionId = Long.parseLong(event.getLocation().getQueryParameters().getSingleParameter("position").get());
		} catch (Exception e) {}
		initComponent();
	}
	
	private void initComponent() {
		setSizeFull();

		configureGrid();
		configureComponents();

		add(new HorizontalLayout(positionsBox, nameSearch, statusBox), grid);
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

		
		List<Position> positionItems = this.positionService.getByCurrentCompany();
		positionsBox.setItems(positionItems);
		positionsBox.setItemLabelGenerator(p -> p.getName());
		positionsBox.setPlaceholder("Position");
		positionsBox.addValueChangeListener(e -> {
			applicatinoFilter.setPosition(e.getValue());
			filterDataProvider.refreshAll();
		});

		nameSearch.setPlaceholder("Candidate name");
		nameSearch.setClearButtonVisible(true);
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			applicatinoFilter.setName(e.getValue());
			filterDataProvider.refreshAll();
		});


		grid.setItems(filterDataProvider);
		
		var optPos = positionItems.stream().filter(p -> p.getId().equals(qPositionId)).findFirst();
		if (optPos.isPresent()) {
			positionsBox.setValue(optPos.get());
		}
	}

	private void configureGrid() {
		grid.addColumn(p -> p.getFullName(), "fullName").setHeader("Full name");
		grid.addColumn(p -> p.getStartedAt(), "startedAt").setHeader("Started at");
		grid.addColumn(p -> p.getSubmittedAt(), "submittedAt").setHeader("Submitted at");
		grid.addComponentColumn(p -> ApplicationStatus.getBadge(p.getStatus())).setHeader("Status");
		grid.addComponentColumn(p -> new Button("Details", e -> UI.getCurrent().navigate(ApplicationMgmtView.class,
				new RouteParameters("appId", p.getId().toString()))));
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
	}
}
