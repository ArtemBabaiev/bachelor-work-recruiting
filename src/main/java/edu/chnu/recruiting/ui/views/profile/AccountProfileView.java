package edu.chnu.recruiting.ui.views.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.exceptions.PasswordException;
import edu.chnu.recruiting.models.formModels.PasswordChangeFormModel;
import edu.chnu.recruiting.models.formModels.UsernameFormModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.services.UserService;
import edu.chnu.recruiting.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile/account", layout = MainLayout.class)
@PermitAll
public class AccountProfileView extends ProfileView {

	private SecurityContext securityContext;

	private UserService userService;

	private User loggedInUser;

	public AccountProfileView(ServiceManager uow) {
		super(uow.getSecurityContext().getAuthenticatedUser());
		this.securityContext = uow.getSecurityContext();
		this.userService = uow.getUserService();
		loggedInUser = this.securityContext.getAuthenticatedUser();
		setContent(getContent());
	}

	private Component getContent() {
		VerticalLayout vl = new VerticalLayout();
		vl.add(getCredentialsChange(), getPasswordChange());
		return vl;
	}

	private Component getCredentialsChange() {
		Details details = new Details();
		details.setOpened(true);
		details.setSummaryText("Change username");
		VerticalLayout content = new VerticalLayout();
		content.addClassName(LumoUtility.Padding.Top.NONE);
		Button changeUsername = new Button("Change username");

		changeUsername.addClickListener(event -> {
			Binder<UsernameFormModel> binder = new BeanValidationBinder<>(UsernameFormModel.class);

			Dialog dialog = new Dialog();
			dialog.setHeaderTitle("Enter a new username");
			Button saveUsername = new Button("Change my username", e -> {
				try {
					this.userService.updateUsername(loggedInUser, binder.getBean().getUsername());
					this.securityContext.logout();
				} catch (Exception ex) {
					Notification.show(ex.getMessage(), 5000, Position.BOTTOM_STRETCH)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});

			TextField username = new TextField("Choose a new username");
			username.setValueChangeMode(ValueChangeMode.EAGER);
			username.setWidthFull();

			binder.bind(username, "username");
			binder.addStatusChangeListener(e -> saveUsername.setEnabled(e.getBinder().isValid()));
			binder.setBean(new UsernameFormModel());

			Span warning = new Span("*You will be logged out after change");
			warning.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-size",
					"var(--lumo-font-size-s)");
			dialog.getFooter().add(new Button("Cancel", e -> dialog.close()));
			dialog.getFooter().add(saveUsername);
			dialog.add(username, warning);
			dialog.open();
		});

		content.add(changeUsername);
		details.add(content);
		content.setWidthFull();
		details.setWidthFull();
		return details;
	}

	private Component getPasswordChange() {
		Details details = new Details();
		details.setOpened(true);
		details.setSummaryText("Change password");
		FormLayout content = new FormLayout();
		//content.addClassName(LumoUtility.Padding.Top.NONE);
		content.setResponsiveSteps(new ResponsiveStep("0", 1));
		PasswordField oldPasswordField = new PasswordField("Old password");
		PasswordField newPasswordField = new PasswordField("New password");
		PasswordField cofirmPasswordField = new PasswordField("Confirm new password");
		oldPasswordField.setValueChangeMode(ValueChangeMode.EAGER);
		newPasswordField.setValueChangeMode(ValueChangeMode.EAGER);
		cofirmPasswordField.setValueChangeMode(ValueChangeMode.EAGER);

		oldPasswordField.setWidthFull();
		newPasswordField.setWidthFull();
		cofirmPasswordField.setWidthFull();

		oldPasswordField.setWidth("400px");
		newPasswordField.setWidth("400px");
		cofirmPasswordField.setWidth("400px");
		
		oldPasswordField.setErrorMessage("wrong password");

		Binder<PasswordChangeFormModel> binder = new BeanValidationBinder<>(PasswordChangeFormModel.class);

		binder.bind(oldPasswordField, "oldPassword");
		binder.bind(newPasswordField, "newPassword");
		binder.forField(cofirmPasswordField)
				.withValidator(e -> e.equals(newPasswordField.getValue()), "Passwords do not match")
				.bind("confirmPassword");

		Button savePassword = new Button("Update password", e -> {
			try {
				if (binder.validate().isOk()) {
					this.userService.updatePassword(loggedInUser, binder.getBean());
					UI.getCurrent().getPage().reload();					
				}
			} catch (PasswordException ex) {
				oldPasswordField.setInvalid(true);
			}
		});

		binder.addStatusChangeListener(e -> savePassword.setEnabled(e.getBinder().isValid()));
		binder.setBean(new PasswordChangeFormModel());
		content.add(oldPasswordField, newPasswordField, cofirmPasswordField, new HorizontalLayout(savePassword));
		details.add(content);
		return details;
	}

}
