package edu.chnu.recruiting.front.views.apply;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.application.ApplicationFormView;
import edu.chnu.recruiting.front.views.application.ApplicationSuccessfullView;
import edu.chnu.recruiting.front.views.apply.ApplyForm.ContinueEvent;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.formModels.ApplyFormModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import edu.chnu.recruiting.utils.enums.SessionKeys;
import jakarta.annotation.security.PermitAll;

@PageTitle("Apply")
@Route(value = "apply", layout = MainLayout.class)
@PermitAll
public class ApplyView extends VerticalLayout implements BeforeEnterObserver {

	private H3 title = new H3("In order to proceed with application, the following information is required");

	private ApplyForm form;

	private ApplicationService applicationService;
	private SecurityContext securityContext;

	private Long positionId;

	public ApplyView(ServiceManager sm) {
		this.applicationService = sm.getApplicationService();
		this.securityContext = sm.getSecurityContext();

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		var session = VaadinSession.getCurrent().getSession();
		positionId = (Long) session.getAttribute(SessionKeys.APPLY_POSITION_ID.toString());
		session.removeAttribute(SessionKeys.APPLY_POSITION_ID.toString());
		if (positionId == null) {
			event.rerouteToError(BadRequestException.class);
		} else {
			initComponent();
		}
	}

	private void initComponent() {
		User user = securityContext.getAuthenticatedUser();
		ApplyFormModel model = new ApplyFormModel();
		model.setDateOfBirth(user.getDateOfBirth());
		model.setEmail(user.getEmail());
		model.setFullName(user.getFullName());

		form = new ApplyForm(model);
		form.setResponsiveSteps(new ResponsiveStep("0", 1));
		form.addContinueListener(e -> handleContinueEvent(e));
		form.addCancelListener(e -> UI.getCurrent().getPage().getHistory().back());
		form.addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Padding.MEDIUM);
		this.setAlignItems(Alignment.CENTER);

		Div test = new Div(title, form);
		test.setMaxWidth("550px");
		test.addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.CENTER, AlignItems.CENTER);
		setSizeFull();
		add(test);

	}

	private void handleContinueEvent(ContinueEvent event) {
		this.apply(event.getModel());
	}

	private void apply(ApplyFormModel model) {
		Application app = this.applicationService.apply(model, positionId);
		if (app.getStatus().equals(ApplicationStatus.PENDING_DATA.toString())) {
			UI.getCurrent().navigate(ApplicationFormView.class, QueryParameters.of("id", app.getId().toString()));
		} else {
			UI.getCurrent().navigate(ApplicationSuccessfullView.class);
		}
	}

}
