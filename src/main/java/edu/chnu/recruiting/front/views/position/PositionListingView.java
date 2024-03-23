package edu.chnu.recruiting.front.views.position;

import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.data.PositionDataProvider;
import edu.chnu.recruiting.front.data.PositionFilter;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.models.Position;

@PageTitle("Positions listing")
@Route(value = "positions", layout = MainLayout.class)
@AnonymousAllowed
public class PositionListingView extends VerticalLayout {
	private Grid<Position> grid;
	private PositionDataProvider dataProvider;
	private PositionFilter positionFilter = new PositionFilter();
	private ConfigurableFilterDataProvider<Position, Void, PositionFilter> filterDataProvider;

	private PositionService positionService;
	private TextField searchField = new TextField();

	public PositionListingView(PositionService positionService) {
		grid = new Grid<>(Position.class, false);
		dataProvider = new PositionDataProvider(positionService);
		filterDataProvider = dataProvider.withConfigurableFilter();
		this.positionService = positionService;
		configureGrid();
		configureComponents();
		add(searchField, grid);
	}

	private void configureComponents() {
		searchField.setWidth("50%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.addValueChangeListener(e -> {
			positionFilter.setSearchTerm(e.getValue());
			filterDataProvider.setFilter(positionFilter);
		});

	}

	private void configureGrid() {
		grid.addColumn(Position::getName, "name").setHeader("Name");
		grid.addColumn(Position::getDescription, "description").setHeader("Description");
		filterDataProvider.setFilter(positionFilter);
		grid.setItems(filterDataProvider);
		
	}
}
