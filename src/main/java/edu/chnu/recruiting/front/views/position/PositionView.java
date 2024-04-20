package edu.chnu.recruiting.front.views.position;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.apply.ApplyView;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import edu.chnu.recruiting.utils.enums.SessionKeys;

@PageTitle("Positions listing")
@Route(value = "positions/:posId", layout = MainLayout.class)
@AnonymousAllowed
public class PositionView extends VerticalLayout implements BeforeEnterObserver {

	private Long posId = null;
	private PositionViewModel model;

	private Button applyBtn = new Button("Apply");

	private PositionService positionService;

	public PositionView(ServiceManager uow) {
		this.positionService = uow.getPositionService();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			this.posId = Long.parseLong(event.getRouteParameters().get("posId").get());
			model = positionService.getPosition(posId, PositionViewModel.class);
		} catch (Exception e) {
			event.rerouteToError(BadRequestException.class);
			return;
		}
		if (model == null) {
			event.rerouteToError(ResourceNotFoundException.class);
			return;
		}
		initComponent();
	}

	private void initComponent() {
		configureComponents();
		add(getContent());
	}

	private void configureComponents() {
		applyBtn.setEnabled(model.getActive());

		applyBtn.addClickListener(e -> {
			VaadinSession.getCurrent().getSession().setAttribute(SessionKeys.APPLY_POSITION_ID.toString(),
					this.model.getId());
			UI.getCurrent().navigate(ApplyView.class);
		});
	}

	private Component getContent() {
		Div content = new Div();
		content.add(getDetailsFor("Name", model.getName()));
		content.add(getDetailsFor("Company", model.getCompanyName()));
		content.add(getDetailsFor("Description", model.getDescription()));
		content.add(getDetailsFor("Employment type", EmploymentType.getLabel(model.getEmploymentType())));
		content.add(getDetailsFor("Location", model.getLocation()));
		content.add(getDetailsFor("Department", model.getDepartment()));
		content.add(getDetailsFor("Salary", getSalaryRepresentation()));
		return content;
	}

	private Details getDetailsFor(String label, String value) {
		Span txt = new Span(value);
		Span summary = new Span(label);
		Details det = new Details(summary, txt);
		det.setOpened(true);
		txt.setClassName(LumoUtility.FontSize.LARGE);
		summary.setClassName(LumoUtility.FontSize.LARGE);
		return det;
	}

	private String getSalaryRepresentation() {
		if (model.getMaxSalary() == null && model.getMinSalary() == null) {
			return "Unspecified";
		} else if (model.getMaxSalary() == null) {
			return model.getMinSalary() + model.getCurrencyCode();
		} else if (model.getMaxSalary() == null) {
			return model.getMinSalary() + model.getCurrencyCode();
		}
		return model.getMinSalary() + "-" + model.getMaxSalary() + " " + model.getCurrencyCode();
	}
}