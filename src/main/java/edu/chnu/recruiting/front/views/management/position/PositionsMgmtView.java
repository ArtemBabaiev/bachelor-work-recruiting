package edu.chnu.recruiting.front.views.management.position;

import java.time.ZoneId;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;

import edu.chnu.recruiting.front.data.IFilter;
import edu.chnu.recruiting.front.data.PositionDataProvider;
import edu.chnu.recruiting.front.data.PositionMgmtFilter;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.management.application.ApplicationsMgmtView;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.utils.DateUtils;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Positions")
@Route(value = "management/positions", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionsMgmtView extends VerticalLayout {
	private Grid<PositionViewModel> grid;
	private PositionDataProvider dataProvider;
	private PositionMgmtFilter positionFilter;
	private ConfigurableFilterDataProvider<PositionViewModel, Void, IFilter<Position>> filterDataProvider;

	private PositionService positionService;
	private Company companyEntity;

	private TextField nameSearch = new TextField();
	private Button createPositionBtn = new Button("Create new position");

	public PositionsMgmtView(ServiceManager uow) {
		this.positionService = uow.getPositionService();

		companyEntity = uow.getCompanyService().getCompanyByUser(uow.getSecurityContext().getAuthenticatedUser());
		grid = new Grid<>(PositionViewModel.class, false);
		dataProvider = new PositionDataProvider(this.positionService);
		positionFilter = new PositionMgmtFilter(companyEntity);
		filterDataProvider = dataProvider.withConfigurableFilter();
		filterDataProvider.setFilter(positionFilter);

		setSizeFull();

		configureGrid();
		configureComponents();

		Div filters = new Div(nameSearch);
		HorizontalLayout controls = new HorizontalLayout(filters);
		controls.setWidthFull();
		controls.expand(filters);
		controls.add(createPositionBtn);
		controls.setAlignItems(Alignment.BASELINE);
		add(controls, grid);
	}

	private void configureComponents() {
		nameSearch.setWidth("30vw");
		nameSearch.setPlaceholder("Search");
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			positionFilter.setNameCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});
		nameSearch.setClearButtonVisible(true);

		createPositionBtn.addClickListener(e -> UI.getCurrent().navigate(PositionCreateView.class));
	}

	private void configureGrid() {
		UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
			String browserTimeZone = extendedClientDetails.getTimeZoneId();
			grid.addColumn(p -> p.getName(), "name").setHeader("Name");
			grid.addColumn(p -> EmploymentType.getLabel(p.getEmploymentType()), "employmentType")
					.setHeader("Employment Type");
			grid.addColumn(p -> p.getDatePosted(), "datePosted").setHeader("Posted at");

			grid.addColumn(p -> {
				var time = p.getUpdatedAt();
				return time != null ? DateUtils.format(time.atZone(ZoneId.of(browserTimeZone))) : null;
			}, "updatedAt").setHeader("Last updated");
			grid.addComponentColumn(p -> getActiveBadge(p)).setHeader("Active");
			grid.addComponentColumn(p -> getControls(p));

			grid.getColumns().forEach(col -> col.setAutoWidth(true));
			grid.setItems(filterDataProvider);
		});

	}

	private Component getControls(PositionViewModel model) {
		return new HorizontalLayout(this.getActivationButton(model), this.getShowApplicationsButton(model),
				this.getDetailsButton(model));
	}

	private Component getShowApplicationsButton(PositionViewModel model) {
		Button btn = new Button("Show applications");
		btn.addClickListener(e -> {
			UI.getCurrent().navigate(ApplicationsMgmtView.class,
					QueryParameters.of("position", model.getId().toString()));
		});
		return btn;
	}

	private Component getActivationButton(PositionViewModel model) {
		Button activationBtn = new Button();
		if (model.getActive()) {
			activationBtn.setText("Deactivate");
			activationBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		} else {
			activationBtn.setText("Activate");
			activationBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		}
		activationBtn.addClickListener(e -> {
			if (model.getActive()) {
				positionService.deactivatePosition(model.getId());
			} else {
				positionService.activatePosition(model.getId());
			}
			filterDataProvider.refreshAll();
		});
		return activationBtn;
	}

	private Button getDetailsButton(PositionViewModel p) {
		Button btn = new Button("Manage", new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
		btn.setIconAfterText(true);
		btn.addClickListener(e -> UI.getCurrent().navigate(PositionMgmtView.class, new RouteParam("id", p.getId())));
		return btn;
	}

	private Icon getActiveBadge(PositionViewModel p) {
		Icon icon;
		if (p.getActive()) {
			icon = VaadinIcon.CHECK.create();
			icon.getElement().getThemeList().add("badge success");
		} else {
			icon = VaadinIcon.CLOSE_SMALL.create();
			icon.getElement().getThemeList().add("badge error");
		}
		icon.getStyle().set("padding", "var(--lumo-space-xs");
		return icon;
	}
}
