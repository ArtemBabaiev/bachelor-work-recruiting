package edu.chnu.recruiting.front.views.company;

import java.util.stream.Stream;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.models.security.User;
import lombok.Getter;

public class CompanyForm extends VerticalLayout {
	private Binder<CompanyFormModel> binder = new BeanValidationBinder<CompanyFormModel>(CompanyFormModel.class);

	private TextField name = new TextField("Company name");
	private MultiSelectComboBox<User> recruiters = new MultiSelectComboBox<>("Recruiters");

	private Button createBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");

	public CompanyForm(CompanyFormModel model, FetchCallback<User, String> fetchCallback,
			SerializableFunction<String, String> filterConverter) {
		binder.bindInstanceFields(this);
		configureComponents();
		this.recruiters.setItemsWithFilterConverter(fetchCallback, filterConverter);
		binder.addStatusChangeListener(e -> createBtn.setEnabled(binder.isValid()));
		binder.setBean(model);

		add(name, recruiters, new HorizontalLayout(cancelBtn, createBtn));

	}

	private void configureComponents() {
		this.setSize(name, recruiters);

		recruiters.setItemLabelGenerator(User::getUsername);

		createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

		createBtn.addClickListener(e -> handleCreateClick(e));
		cancelBtn.addClickListener(e -> fireEvent(new CancelEvent(this)));
	}

	private void handleCreateClick(ClickEvent<Button> e) {
		if (binder.isValid())
			fireEvent(new SaveEvent(this, binder.getBean()));
	}

	private void setSize(HasSize... components) {
		Stream.of(components).forEach(comp -> comp.setWidth("315px"));
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
