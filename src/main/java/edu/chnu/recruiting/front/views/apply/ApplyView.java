package edu.chnu.recruiting.front.views.apply;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.front.views.application.ApplicationFormView;
import edu.chnu.recruiting.front.views.application.ApplicationSuccessfullView;
import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.formModels.ApplicationFormModel;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.utils.UiUtils;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;
import edu.chnu.recruiting.utils.enums.SessionKeys;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Apply")
@Route(value = "apply", layout = MainLayout.class)
@RolesAllowed({"USER"})
public class ApplyView extends VerticalLayout {
	private Binder<ApplicationFormModel> binder = new BeanValidationBinder<ApplicationFormModel>(
			ApplicationFormModel.class);

	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");
	private DatePicker dateOfBirth = new DatePicker("Date of birth");

	private Button continueBtn = new Button("Continue");
	private Button cancelBtn = new Button("Cancel");

	private ApplicationService applicationService;

	private Long positionId;

	public ApplyView(ApplicationService applicationService) {
		var session = VaadinSession.getCurrent().getSession();
		positionId = (Long) session.getAttribute(SessionKeys.APPLY_POSITION_ID.toString());
		if (positionId == null) {
			throw new BadRequestException();
		}
		session.removeAttribute(SessionKeys.APPLY_POSITION_ID.toString());
		this.applicationService = applicationService;

		UiUtils.setWidth("30vw", firstName, lastName, dateOfBirth);

		DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
		multiFormatI18n.setDateFormats("dd.MM.yyyy", "MM/dd/yyyy");
		dateOfBirth.setI18n(multiFormatI18n);

		HorizontalLayout controls = new HorizontalLayout(cancelBtn, continueBtn);
		cancelBtn.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());
		continueBtn.addClickListener(e -> handleContinueClick(e));

		setSizeFull();
		setAlignItems(Alignment.CENTER);
		add(firstName, lastName, dateOfBirth, controls);

		binder.bindInstanceFields(this);
		binder.addStatusChangeListener(e -> continueBtn.setEnabled(binder.isValid()));
		binder.setBean(new ApplicationFormModel());

	}

	private void handleContinueClick(ClickEvent<Button> e) {
		Application app = this.applicationService.apply(binder.getBean(), positionId);
		if (app.getStatus().equals(ApplicationStatus.PENDING_DATA.toString())) {
			UI.getCurrent().navigate(ApplicationFormView.class, QueryParameters.of("id", app.getId().toString()));
		} else {
			UI.getCurrent().navigate(ApplicationSuccessfullView.class);
		}
	}

}
