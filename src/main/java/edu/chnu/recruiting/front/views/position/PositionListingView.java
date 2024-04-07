package edu.chnu.recruiting.front.views.position;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.data.PositionDataProvider;
import edu.chnu.recruiting.front.data.PositionFilter;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.utils.enums.EmploymentType;

@PageTitle("Positions listing")
@Route(value = "positions", layout = MainLayout.class)
@AnonymousAllowed
@CssImport(value = "./themes/recruiting/styles.css", themeFor = "vaadin-grid")
public class PositionListingView extends VerticalLayout {
	private Grid<PositionViewModel> grid;
	private PositionDataProvider dataProvider;
	private PositionFilter positionFilter = new PositionFilter();
	private ConfigurableFilterDataProvider<PositionViewModel, Void, PositionFilter> filterDataProvider;

	private PositionService positionService;
	private TextField nameSearch = new TextField();
	private Checkbox activeSearch = new Checkbox("Only Active");

	public PositionListingView(PositionService positionService) {
		this.positionService = positionService;

		grid = new Grid<>(PositionViewModel.class, false);
		dataProvider = new PositionDataProvider(this.positionService);
		filterDataProvider = dataProvider.withConfigurableFilter();
		
		setSizeFull();
		
		configureGrid();
		configureComponents();

		HorizontalLayout filters = new HorizontalLayout(nameSearch, activeSearch);
		filters.setAlignItems(Alignment.BASELINE);
		add(filters, grid);
	}

	private void configureComponents() {
		nameSearch.setWidth("30vw");
		nameSearch.setPlaceholder("Search");
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			positionFilter.setNameCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});

		activeSearch.addValueChangeListener(e -> {
			positionFilter.setActiveCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});

	}

	private void configureGrid() {
		filterDataProvider.setFilter(positionFilter);

		grid.addColumn(p -> p.getName(), "name").setHeader("Name");
		grid.addColumn(p -> p.getCompanyName(), "companyName").setHeader("Company");
		grid.addColumn(p -> EmploymentType.getLabel(p.getEmploymentType()), "employmentType")
				.setHeader("Employment Type");
		grid.addColumn(p -> p.getDatePosted(), "datePosted").setHeader("Posted at");
		grid.addComponentColumn(p -> getActiveBadge(p)).setHeader("Active");
		grid.addComponentColumn(p -> getDetailsButton(p));
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		
		grid.setItems(filterDataProvider);

	}

	private Button getDetailsButton(PositionViewModel p) {
		Button btn = new Button("Details", new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
		btn.setIconAfterText(true);
		btn.addClickListener(e -> UI.getCurrent().navigate(PositionView.class, new RouteParam("posId", p.getId())));
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
