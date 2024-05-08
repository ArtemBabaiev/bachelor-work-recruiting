package edu.chnu.recruiting.ui.views.open;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.CompanyViewModel;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.data.IFilter;
import edu.chnu.recruiting.ui.data.PositionCompanyFilter;
import edu.chnu.recruiting.ui.data.PositionDataProvider;
import edu.chnu.recruiting.utils.enums.EmploymentType;

@Route(value = "companies/:id", layout = MainLayout.class)
@AnonymousAllowed
public class CompanyView extends SplitLayout implements BeforeEnterObserver, HasDynamicTitle {

	private CompanyViewModel model;

	private Grid<PositionViewModel> grid;
	private PositionDataProvider dataProvider;
	private PositionCompanyFilter positionFilter;
	private ConfigurableFilterDataProvider<PositionViewModel, Void, IFilter<Position>> filterDataProvider;

	private CompanyService companyService;
	private PositionService positionService;

	private TextField nameSearch = new TextField();

	public CompanyView(CompanyService companyService, PositionService positionService) {
		this.companyService = companyService;
		this.positionService = positionService;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Long id;
		try {
			id = event.getRouteParameters().getLong("id").get();
		} catch (Exception e) {
			event.rerouteToError(BadRequestException.class);
			return;
		}
		model = this.companyService.getCompany(id, CompanyViewModel.class);
		if (model == null) {
			event.rerouteToError(ResourceNotFoundException.class);
		} else {
			initComponent();
		}

	}

	@Override
	public String getPageTitle() {
		return "Company: " + model.getName();
	}

	private void initComponent() {
		grid = new Grid<>(PositionViewModel.class, false);
		positionFilter = new PositionCompanyFilter(model.getId());
		dataProvider = new PositionDataProvider(this.positionService);
		filterDataProvider = dataProvider.withConfigurableFilter();
		filterDataProvider.setFilter(positionFilter);

		setSizeFull();

		Component companyInfo = getCompanyInfo();
		configureGrid();
		configureComponents();

		HorizontalLayout filters = new HorizontalLayout();
		filters.addAndExpand(nameSearch);
		filters.setAlignItems(Alignment.BASELINE);
		grid.setSizeFull();
		addToPrimary(companyInfo);
		addToSecondary(new VerticalLayout(filters, grid));
		setSplitterPosition(30);

	}

	private Component getCompanyInfo() {
		VerticalLayout vl = new VerticalLayout();
		vl.addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.BorderRadius.MEDIUM);
		vl.setWidthFull();
		H2 name = new H2(model.getName());
		vl.add(name);
		vl.add(getCompanyItem(VaadinIcon.FACTORY, model.getIndustry()));
		vl.add(getCompanyItem(VaadinIcon.MAP_MARKER, model.getAddress()));
		vl.add(getCompanyItem(VaadinIcon.AT, model.getEmail()));
		vl.add(getCompanyItem(VaadinIcon.PHONE, model.getContactPhone()));
		vl.add(getDescription(model.getDescription()));
		return vl;
	}

	private void configureComponents() {
		nameSearch.setMaxWidth("450px");
		nameSearch.setPlaceholder("Search");
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			positionFilter.setNameCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});
	}

	private void configureGrid() {
		grid.addColumn(p -> p.getName(), "name").setHeader("Name");
		grid.addColumn(p -> p.getDepartment(), "department").setHeader("Department");
		grid.addColumn(p -> EmploymentType.getLabel(p.getEmploymentType()), "employmentType")
				.setHeader("Employment Type");
		grid.addColumn(p -> getSalaryRepresentation(p)).setHeader("Salary Range");
		grid.addColumn(p -> p.getDatePosted(), "datePosted").setHeader("Posted at");
		grid.addComponentColumn(p -> getDetailsButton(p));
		grid.getColumns().forEach(col -> col.setAutoWidth(true));

		grid.setItems(filterDataProvider);

	}

	private String getSalaryRepresentation(PositionViewModel position) {
		if (position.getMaxSalary() == null && position.getMinSalary() == null) {
			return "Unspecified";
		} else if (position.getMaxSalary() == null) {
			return position.getMinSalary() + position.getCurrencyCode();
		} else if (position.getMaxSalary() == null) {
			return position.getMinSalary() + position.getCurrencyCode();
		}
		return position.getMinSalary() + "-" + position.getMaxSalary() + " " + position.getCurrencyCode();
	}

	private Button getDetailsButton(PositionViewModel p) {
		Button btn = new Button("Details", new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
		btn.setIconAfterText(true);
		btn.addClickListener(e -> UI.getCurrent().navigate(PositionView.class, new RouteParam("posId", p.getId())));
		return btn;
	}

	private Component getCompanyItem(VaadinIcon vaadinIcon, String value) {
		var icon = vaadinIcon.create();
		Span text = new Span(value);
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getDescription(String value) {
		Div desc = new Div();
		desc.add(new H3("Description"));
		var text = new Paragraph(value);
		text.addClassName(LumoUtility.FontSize.LARGE);
		desc.add(text);
		desc.setSizeFull();
		return desc;
	}
}
