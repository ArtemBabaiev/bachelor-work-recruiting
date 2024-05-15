package edu.chnu.recruiting.ui.views.management.company;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import lombok.Getter;

public class CompanyForm extends FormLayout {
	private Binder<CompanyFormModel> binder = new BeanValidationBinder<CompanyFormModel>(CompanyFormModel.class);

	private TextField name = new TextField("Company name");
	private TextArea description = new TextArea("About us/Description");
	private TextField industry = new TextField("Industry(-ies)");
	private TextField contactPhone = new TextField("Contact phone");
	private EmailField email = new EmailField("Contact email");
	private TextField address = new TextField("Address");

	private Button createBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");

	public CompanyForm(CompanyFormModel model) {
		binder.bindInstanceFields(this);
		configureComponents();

		binder.setBean(model);

		add(name, description, industry, contactPhone, email, address, new HorizontalLayout(cancelBtn, createBtn));

	}

	private void configureComponents() {
		setResponsiveSteps(new ResponsiveStep("0", 1));

		createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

		contactPhone.setAllowedCharPattern("[0-9()+-]");

		createBtn.addClickListener(e -> handleCreateClick(e));
		cancelBtn.addClickListener(e -> fireEvent(new CancelEvent(this)));
		
		name.setPlaceholder("Enter company name");
		description.setPlaceholder("Describe your company");
		industry.setPlaceholder("e.g., IT, Trading");
		contactPhone.setPlaceholder("Enter phone number");
		email.setPlaceholder("Enter email address (e.g., contact@yourcompany.com)");
		address.setPlaceholder("Enter your company's physical address");
	}

	public void setBean(CompanyFormModel bean) {
		binder.setBean(bean);
	}

	private void handleCreateClick(ClickEvent<Button> e) {
		if (binder.validate().isOk())
			fireEvent(new SaveEvent(this, binder.getBean()));
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	@Getter
	public static abstract class CompanyFormEvent extends ComponentEvent<CompanyForm> {
		private CompanyFormModel model;

		protected CompanyFormEvent(CompanyForm source, CompanyFormModel model) {
			super(source, false);
			this.model = model;
		}
	}

	public static class SaveEvent extends CompanyFormEvent {

		SaveEvent(CompanyForm source, CompanyFormModel model) {
			super(source, model);
		}
	}

	public static class CancelEvent extends CompanyFormEvent {

		CancelEvent(CompanyForm source) {
			super(source, null);
		}
	}
}
