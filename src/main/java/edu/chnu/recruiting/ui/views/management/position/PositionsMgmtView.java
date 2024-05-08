package edu.chnu.recruiting.ui.views.management.position;

import java.time.ZoneId;

import org.vaadin.lineawesome.LineAwesomeIcon;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;

import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.data.IFilter;
import edu.chnu.recruiting.ui.data.PositionDataProvider;
import edu.chnu.recruiting.ui.data.PositionMgmtFilter;
import edu.chnu.recruiting.ui.views.management.application.ApplicationsMgmtView;
import edu.chnu.recruiting.utils.DateUtils;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Positions Management")
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
	private ComboBox<EmploymentType> employmentFilter = new ComboBox<EmploymentType>();
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

		HorizontalLayout controls = new HorizontalLayout();
		controls.setWidthFull();
		controls.addAndExpand(nameSearch);
		controls.add(employmentFilter);
		controls.addAndExpand(new Span());
		controls.add(createPositionBtn);
		controls.setAlignItems(Alignment.BASELINE);
		add(controls, grid);
	}

	private void configureComponents() {
		nameSearch.setMaxWidth("450px");
		nameSearch.setPlaceholder("Search");
		nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		nameSearch.addValueChangeListener(e -> {
			positionFilter.setNameCriteria(e.getValue());
			filterDataProvider.refreshAll();
		});
		nameSearch.setClearButtonVisible(true);
		
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

		createPositionBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
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
			grid.addComponentColumn(p -> getActivationButton(p)).setHeader("Activation");
			grid.addComponentColumn(p -> getControls(p));

			grid.getColumns().forEach(col -> col.setAutoWidth(true));
			grid.setItems(filterDataProvider);
		});

	}

	private Component getControls(PositionViewModel model) {
		return new HorizontalLayout(this.getDetailsButton(model), this.getShowApplicationsButton(model));
	}

	private Component getShowApplicationsButton(PositionViewModel model) {
		Button btn = new Button("Applications", VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
		btn.addThemeVariants(ButtonVariant.LUMO_ICON);
		btn.setIconAfterText(true);
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
		Button btn = new Button(LineAwesomeIcon.EDIT.create(),
				e -> UI.getCurrent().navigate(PositionMgmtView.class, new RouteParam("id", p.getId())));
		btn.addThemeVariants(ButtonVariant.LUMO_ICON);
		return btn;
	}
}
