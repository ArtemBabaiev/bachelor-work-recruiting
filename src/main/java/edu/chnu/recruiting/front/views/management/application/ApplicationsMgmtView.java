package edu.chnu.recruiting.front.views.management.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import edu.chnu.recruiting.front.data.ApplicationDataProvider;
import edu.chnu.recruiting.front.data.ApplicationFilter;
import edu.chnu.recruiting.front.data.PositionDataProvider;
import edu.chnu.recruiting.front.data.PositionFilter;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.TestExampleView;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.ApplicationGridVM;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.UnitOfWork;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@PageTitle("Applications")
@Route(value = "management/applications", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class ApplicationsMgmtView extends VerticalLayout {
	private PositionService positionService;
	private ApplicationService applicationService;

	private Grid<ApplicationGridVM> grid;
	private ApplicationDataProvider dataProvider;
	private ApplicationFilter applicatinoFilter = new ApplicationFilter();
	private ConfigurableFilterDataProvider<ApplicationGridVM, Void, ApplicationFilter> filterDataProvider;

	private TextField nameSearch = new TextField();
	private ComboBox<Position> positionsBox = new ComboBox<Position>();
	private ComboBox<String> statusBox = new ComboBox<String>();

	public ApplicationsMgmtView(UnitOfWork uow) {
		this.applicationService = uow.getApplicationService();
		this.positionService = uow.getPositionService();

		grid = new Grid<>(ApplicationGridVM.class, false);
		dataProvider = new ApplicationDataProvider(this.applicationService);
		filterDataProvider = dataProvider.withConfigurableFilter();
		filterDataProvider.setFilter(applicatinoFilter);

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

		positionsBox.setItems(this.positionService.getByCurrentCompany());
		positionsBox.setItemLabelGenerator(p -> p.getName());
		positionsBox.setPlaceholder("Position");

		nameSearch.setPlaceholder("Candidate name");
		nameSearch.setClearButtonVisible(true);
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			applicatinoFilter.setName(e.getValue());
			filterDataProvider.refreshAll();
		});

		positionsBox.addValueChangeListener(e -> {
			applicatinoFilter.setPosition(e.getValue());
			filterDataProvider.refreshAll();
		});

		grid.setItems(filterDataProvider);

	}

	private void configureGrid() {
		grid.addColumn(p -> p.getFirstName(), "firstName").setHeader("First name");
		grid.addColumn(p -> p.getLastName(), "lastName").setHeader("Last name");
		grid.addColumn(p -> p.getStartedAt(), "startedAt").setHeader("Started at");
		grid.addColumn(p -> p.getSubmittedAt(), "submittedAt").setHeader("Submitted at");
		grid.addComponentColumn(p -> getStatusBadge(p.getStatus())).setHeader("Status");
		grid.addComponentColumn(p -> new Button("Details", e -> UI.getCurrent().navigate(ApplicationMgmtView.class,
				new RouteParameters("appId", p.getId().toString()))));
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
	}

	private Component getStatusBadge(String status) {
		Span badge = null;
		switch (ApplicationStatus.valueOf(status)) {
		case ACCEPTED:
			badge = new Span(ApplicationStatus.ACCEPTED.getLabel());
			badge.getElement().getThemeList().add("badge success");
			break;
		case PENDING_DATA:
			badge = new Span(ApplicationStatus.ACCEPTED.getLabel());
			badge.getElement().getThemeList().add("badge contrast");
			break;
		case PENDING_REVIEW:
			badge = new Span(ApplicationStatus.PENDING_REVIEW.getLabel());
			badge.getElement().getThemeList().add("badge");
			break;
		case REJECTED:
			badge = new Span(ApplicationStatus.REJECTED.getLabel());
			badge.getElement().getThemeList().add("badge error");
			break;
		}
		return badge;
	}
}
