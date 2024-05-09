package edu.chnu.recruiting.ui.views.open;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.exceptions.ResourceNotFoundException;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.apply.ApplyView;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import edu.chnu.recruiting.utils.enums.SessionKeys;

@Route(value = "positions/:posId", layout = MainLayout.class)
@AnonymousAllowed
public class PositionView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

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

	@Override
	public String getPageTitle() {
		return "Position: " + model.getName();
	}

	private void initComponent() {
		VerticalLayout content = getContent();
		content.addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.BorderRadius.MEDIUM);
		content.setMaxWidth("650px");
		configureComponents();
		this.setAlignItems(Alignment.CENTER);
		this.setSizeFull();
		add(content);
	}

	private void configureComponents() {
		applyBtn.setEnabled(model.getActive());
		applyBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		applyBtn.addClickListener(e -> {
			VaadinSession.getCurrent().getSession().setAttribute(SessionKeys.APPLY_POSITION_ID.toString(),
					this.model.getId());
			UI.getCurrent().navigate(ApplyView.class);
		});
	}

	private VerticalLayout getContent() {
		VerticalLayout content = new VerticalLayout();

		H2 positionName = new H2(model.getName());

		HorizontalLayout controls = new HorizontalLayout();
		controls.addClassName(LumoUtility.Padding.NONE);
		controls.addAndExpand(positionName);
		controls.add(applyBtn);

		content.add(controls);
		content.add(getSalary());
		content.add(getCompany());
		content.add(getDepartment());
		content.add(getLocation());
		content.add(getEmail());
		content.add(getPhone());
		content.add(getEmploymentType());
		content.add(getDescription());
		return content;
	}

	private Component getSalary() {
		var icon = VaadinIcon.MONEY.create();
		Span text = new Span(getSalaryRepresentation());
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getCompany() {
		var icon = VaadinIcon.OFFICE.create();
		Button text = new Button(model.getCompanyName());
		text.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_CONTRAST);
		text.addClassNames(LumoUtility.FontSize.LARGE, "underlined");
		text.addClickListener(
				e -> UI.getCurrent().navigate(CompanyView.class, new RouteParam("id", model.getCompanyId())));
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getDepartment() {
		var icon = VaadinIcon.SUITCASE.create();
		Span text = new Span(model.getDepartment());
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getLocation() {
		var icon = VaadinIcon.MAP_MARKER.create();
		Span text = new Span(model.getLocation());
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getEmail() {
		var icon = VaadinIcon.AT.create();
		Span text = new Span(model.getCompanyEmail());
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getPhone() {
		var icon = VaadinIcon.PHONE.create();
		Span text = new Span(model.getCompanyContactPhone());
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getEmploymentType() {
		var icon = VaadinIcon.CLOCK.create();
		Span text = new Span(EmploymentType.getLabel(model.getEmploymentType()));
		text.addClassName(LumoUtility.FontSize.LARGE);
		HorizontalLayout hl = new HorizontalLayout(icon, text);
		hl.addClassName(LumoUtility.Padding.NONE);
		hl.setAlignItems(Alignment.CENTER);
		return hl;
	}

	private Component getDescription() {
		Div desc = new Div();
		desc.add(new H3("Description"));
		var text = new Paragraph(model.getDescription());
		text.addClassName(LumoUtility.FontSize.LARGE);
		desc.add(text);
		desc.setSizeFull();
		return desc;
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