package edu.chnu.recruiting.ui.views.open;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.data.IFilter;
import edu.chnu.recruiting.ui.data.PositionDataProvider;
import edu.chnu.recruiting.ui.data.PositionFilter;
import edu.chnu.recruiting.utils.enums.EmploymentType;

@PageTitle("Positions listing")
@Route(value = "positions", layout = MainLayout.class)
@AnonymousAllowed
public class PositionListingView extends VerticalLayout implements BeforeEnterObserver{
	private Grid<PositionViewModel> grid;
	private PositionDataProvider dataProvider;
	private PositionFilter positionFilter = new PositionFilter();
	private ConfigurableFilterDataProvider<PositionViewModel, Void, IFilter<Position>> filterDataProvider;

	private PositionService positionService;
	private TextField nameSearch = new TextField();
	private TextField companySearch = new TextField();
	private ComboBox<EmploymentType> employmentFilter = new ComboBox<EmploymentType>();
	
	private String qPositionName;

	public PositionListingView(PositionService positionService) {
		this.positionService = positionService;
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		qPositionName = event.getLocation().getQueryParameters().getSingleParameter("positionName").orElse(null);
		initComponent();
	}

	private void initComponent() {
		grid = new Grid<>(PositionViewModel.class, false);
		dataProvider = new PositionDataProvider(this.positionService);
		filterDataProvider = dataProvider.withConfigurableFilter();
		
		setSizeFull();
		
		configureGrid();
		configureComponents();
		if (qPositionName != null && !qPositionName.isEmpty()) {
			nameSearch.setValue(qPositionName);			
		}
		
		HorizontalLayout filters = new HorizontalLayout();
		filters.addClassName(LumoUtility.FlexWrap.WRAP);
		filters.addAndExpand(nameSearch, companySearch);
		filters.add(employmentFilter);
		filters.setAlignItems(Alignment.BASELINE);
		add(filters, grid);
	}

	private void configureComponents() {
		nameSearch.setMaxWidth("450px");
		nameSearch.setPlaceholder("Position name");
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			positionFilter.setNameCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});
		nameSearch.setClearButtonVisible(true);
		
		companySearch.setMaxWidth("450px");
		companySearch.setPlaceholder("Company name");
		companySearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		companySearch.addValueChangeListener(e -> {
			positionFilter.setCompanyNameCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});
		companySearch.setClearButtonVisible(true);
		
		employmentFilter.setPlaceholder("Employment type");
		employmentFilter.setItems(EmploymentType.values());
		employmentFilter.setItemLabelGenerator(EmploymentType::getLabel);
		employmentFilter.setClearButtonVisible(true);
		employmentFilter.addValueChangeListener(e -> {
			if (e.getValue() != null) {
				positionFilter.setEmploymentTypeCriteria(e.getValue().getValue());				
			} else {
				positionFilter.setEmploymentTypeCriteria(null);		
			}
			filterDataProvider.refreshAll();
		});

	}

	private void configureGrid() {
		filterDataProvider.setFilter(positionFilter);

		grid.addColumn(p -> p.getName(), "name").setHeader("Name");
		grid.addComponentColumn(p -> getCompanyColumn(p)).setSortProperty("companyName").setHeader("Company");
		grid.addColumn(p -> p.getDepartment(), "department").setHeader("Department");
		grid.addColumn(p -> EmploymentType.getLabel(p.getEmploymentType()), "employmentType")
				.setHeader("Employment Type");
		grid.addColumn(p -> p.getDatePosted(), "datePosted").setHeader("Posted at");
		grid.addComponentColumn(p -> getDetailsButton(p));
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		
		grid.setItems(filterDataProvider);

	}

	private Component getCompanyColumn(PositionViewModel position) {
		SideNavItem item = new SideNavItem(position.getCompanyName());
		item.setPath(CompanyView.class, new RouteParameters("id", position.getCompanyId().toString()));
		return item;
	}

	private Button getDetailsButton(PositionViewModel p) {
		Button btn = new Button("Details", new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
		btn.setIconAfterText(true);
		btn.addClickListener(e -> UI.getCurrent().navigate(PositionView.class, new RouteParam("posId", p.getId())));
		return btn;
	}
}
