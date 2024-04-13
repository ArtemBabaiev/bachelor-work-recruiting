package edu.chnu.recruiting.front.views.application;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.components.AudioRecorder;
import edu.chnu.recruiting.front.components.AudioTag;
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
		nextBtn.addClickListener(e -> {
			fireEvent(new NextEvent(this, binder.getBean()));
		});
		backBtn.addClickListener(e -> {
			fireEvent(new BackEvent(this, binder.getBean()));
		});
		backBtn.setEnabled(model.getId() > 0);
		backBtn.setVisible(model.getId() > 0);

		add(new H2("Step " + (model.getId() + 1) + ": " + model.getName()));

		for (var field : model.getFields()) {
			configureField(field);
		}
		nextBtn.setEnabled(binder.isValid());;
		add(new HorizontalLayout(backBtn, nextBtn));

	}

	private void configureField(WizardField field) {
		switch (field.getType()) {
		case AUDIO:
			add(getAudio(field));
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
			add(getUpload(field));
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

	private CheckboxGroup<String> getMultiSelection(WizardField field) {
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

	private RadioButtonGroup<String> getSingleSelection(WizardField field) {
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

	private Component getUpload(WizardField field) {
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		Upload singleFileUpload = new Upload(memoryBuffer);
		Span currentUpload = new Span();
		setCurrentUpload(currentUpload, field.getFileName());
		H4 question = new H4(field.getQuestion() + (field.isRequired() ? "*" : ""));
		Span maxSizeMessage = new Span("Maximum file size: 20 MB");
		singleFileUpload.addSucceededListener(e -> {
			InputStream fileData = memoryBuffer.getInputStream();
			try {
				field.setUserValue(fileData.readAllBytes());
				field.setFileName(e.getFileName());
				setCurrentUpload(currentUpload, field.getFileName());
				binder.validate();
			} catch (IOException e1) {
			}
		});
		singleFileUpload.getElement().addEventListener("file-remove", new DomEventListener() {
			@Override
			public void handleEvent(DomEvent arg0) {
				field.setFileName(null);
				field.setUserValue(null);
				setCurrentUpload(currentUpload, field.getFileName());
				binder.validate();
			}
		});
		singleFileUpload.setMaxFileSize(20_971_520);
		Div content = new Div(question, maxSizeMessage, singleFileUpload, currentUpload);
		if (field.isRequired()) {
			binder.withValidator(step -> step.getFieldValue(field.getId()) != null, "File upload is required");
		}
		return content;
	}

	private Component getAudio(WizardField field) {
		AudioRecorder recorder = new AudioRecorder();
		recorder.addRecordedListener(e -> {		
			field.setUserValue(e.getRecording());
			binder.validate();
		});

		if (field.isRequired()) {
			binder.withValidator(step -> step.getFieldValue(field.getId()) != null, "Audio is required");
		}
		VerticalLayout question = new VerticalLayout();
		question.addClassNames(LumoUtility.Padding.NONE);
		if (field.isTextToSpeech()) {
			AudioTag audio = new AudioTag(field.getSpeech());
			question.add(new Span("Click play to hear the question"), audio);

		} else {
			question.add(new Span(field.getQuestion()));
		}
		question.add(recorder);
		return question;
	}

	private void setCurrentUpload(Span message, String name) {
		String savedFileTemplate = "Saved file: %s";
		if (name == null) {
			message.setText(savedFileTemplate.formatted(""));
		} else {
			message.setText(savedFileTemplate.formatted(name));
		}
	}

	public Registration addNextListener(ComponentEventListener<NextEvent> listener) {
		return addListener(NextEvent.class, listener);
	}

	public Registration addBackListener(ComponentEventListener<BackEvent> listener) {
		return addListener(BackEvent.class, listener);
	}

	@Getter
	public static abstract class SectionFormEvent extends ComponentEvent<SectionForm> {
		private WizardStep step;

		protected SectionFormEvent(SectionForm source, WizardStep step) {
			super(source, false);
			this.step = step;
		}
	}

	public static class NextEvent extends SectionFormEvent {

		NextEvent(SectionForm source, WizardStep step) {
			super(source, step);
		}
	}

	public static class BackEvent extends SectionFormEvent {

		BackEvent(SectionForm source, WizardStep step) {
			super(source, step);
		}
	}
}
