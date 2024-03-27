package edu.chnu.recruiting.front.views.application;

import java.time.LocalDate;
import java.util.Set;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.models.wizard.WizardField;
import edu.chnu.recruiting.models.wizard.WizardStep;
import lombok.Getter;

public class SectionForm extends VerticalLayout {
	private Binder<WizardStep> binder = new BeanValidationBinder<WizardStep>(WizardStep.class);
	private WizardStep model;

	private Button nextBtn = new Button("Next");
	private Button backBtn = new Button("Back");

	public SectionForm(WizardStep model) {
		this.model = model;
		initComponent();
	}

	private void initComponent() {
		binder.setBean(model);
		binder.addStatusChangeListener(e -> nextBtn.setEnabled(binder.isValid()));

		nextBtn.addClickListener(e -> fireEvent(new NextEvent(this, model.getId())));
		backBtn.addClickListener(e -> fireEvent(new BackEvent(this, model.getId())));
		backBtn.setEnabled(model.getId() > 0);
		backBtn.setVisible(model.getId() > 0);
		
		for (var field : model.getFields()) {
			configureField(field);
		}
		add(new HorizontalLayout(backBtn, nextBtn));

	}

	private void configureField(WizardField field) {
		switch (field.getType()) {
		case AUDIO:
			break;
		case DATE:
			add(getDate(field));
			break;
		case NUMBER:
			add(getNumber(field));
			break;
		case SELECTION_MULTIPLE:
			add(getMultiSelection(field));
			break;
		case SELECTION_SINGLE:
			add(getSingleSelection(field));
			break;
		case TEXT:
			add(getText(field));
			break;
		case UPLOAD:
			break;

		}
	}

	private TextField getText(WizardField field) {
		TextField tf = new TextField(field.getQuestion());
		tf.setWidth("30vw");
		BindingBuilder<WizardStep, String> builder = binder.forField(tf);
		if (field.isRequired()) {
			builder.asRequired();
		}
		builder.bind(step -> (String) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));

		return tf;
	}

	private NumberField getNumber(WizardField field) {
		NumberField f = new NumberField(field.getQuestion());
		f.setWidth("30vw");
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired();
		}
		builder.bind(step -> (Double) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}

	private DatePicker getDate(WizardField field) {
		DatePicker f = new DatePicker(field.getQuestion());
		f.setWidth("30vw");
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired();
		}
		builder.bind(step -> (LocalDate) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}

	private CheckboxGroup getMultiSelection(WizardField field) {
		CheckboxGroup<String> f = new CheckboxGroup<String>(field.getQuestion());
		f.setWidth("30vw");
		f.setItems(field.getOptions());
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired();
		}
		builder.bind(step -> (Set<String>) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}

	private RadioButtonGroup getSingleSelection(WizardField field) {
		RadioButtonGroup<String> f = new RadioButtonGroup<String>(field.getQuestion());
		f.setWidth("30vw");
		f.setItems(field.getOptions());
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired();
		}
		builder.bind(step -> (String) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}
	
	public Registration addNextListener(ComponentEventListener<NextEvent> listener) {
		return addListener(NextEvent.class, listener);
	}

	public Registration addBackListener(ComponentEventListener<BackEvent> listener) {
		return addListener(BackEvent.class, listener);
	}

	@Getter
	public static abstract class SectionFormEvent extends ComponentEvent<SectionForm> {
		private int stepId;
		protected SectionFormEvent(SectionForm source, int stepId) {
			super(source, false);
			this.stepId = stepId;
		}
	}

	public static class NextEvent extends SectionFormEvent {

		NextEvent(SectionForm source, int stepId) {
			super(source, stepId);
		}
	}

	public static class BackEvent extends SectionFormEvent {

		BackEvent(SectionForm source, int stepId) {
			super(source, stepId);
		}
	}
}
