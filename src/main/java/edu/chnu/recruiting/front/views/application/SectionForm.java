package edu.chnu.recruiting.front.views.application;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
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
		this.addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.BorderRadius.MEDIUM);
		binder.addStatusChangeListener(e -> nextBtn.setEnabled(binder.isValid()));
		binder.setBean(model);
		nextBtn.addClickListener(e -> {
			if (binder.validate().isOk()) {
				fireEvent(new NextEvent(this, binder.getBean()));
			}
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
		add(new HorizontalLayout(backBtn, nextBtn));

	}

	private void configureField(WizardField field) {
		Component toAdd = null;
		switch (field.getType()) {
		case AUDIO:
			toAdd = getAudio(field);
			break;
		case DATE:
			toAdd = getDate(field);
			break;
		case NUMBER:
			toAdd = getNumber(field);
			break;
		case SELECTION_MULTIPLE:
			toAdd = getMultiSelection(field);
			break;
		case SELECTION_SINGLE:
			toAdd = getSingleSelection(field);
			break;
		case TEXT:
			toAdd = getText(field);
			break;
		case UPLOAD:
			toAdd = getUpload(field);
			break;

		}
		((HasSize) toAdd).setWidthFull();
		add(toAdd, new Hr());
	}

	private TextField getText(WizardField field) {
		TextField tf = new TextField(field.getQuestion());
		BindingBuilder<WizardStep, String> builder = binder.forField(tf);
		if (field.isRequired()) {
			builder.asRequired("Required");
		}
		builder.bind(step -> (String) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));

		return tf;
	}

	private NumberField getNumber(WizardField field) {
		NumberField f = new NumberField(field.getQuestion());
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired("Required");
		}
		builder.bind(step -> (Double) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}

	private DatePicker getDate(WizardField field) {
		DatePicker f = new DatePicker(field.getQuestion());
		DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
		multiFormatI18n.setDateFormats("dd.MM.yyyy", "MM/dd/yyyy");
		f.setI18n(multiFormatI18n);
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired("Required");
		}
		builder.bind(step -> (LocalDate) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}

	private CheckboxGroup<String> getMultiSelection(WizardField field) {
		CheckboxGroup<String> f = new CheckboxGroup<String>(field.getQuestion());
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
		f.setItems(field.getOptions());
		var builder = binder.forField(f);
		if (field.isRequired()) {
			builder.asRequired("Required");
		}
		builder.bind(step -> (String) step.getFieldValue(field.getId()),
				(step, value) -> step.setFieldValue(field.getId(), value));
		return f;
	}

	private Component getUpload(WizardField field) {
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		Upload singleFileUpload = new Upload(memoryBuffer);
		Span currentUpload = new Span();
		currentUpload.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-size",
				"var(--lumo-font-size-s)");
		setCurrentUpload(currentUpload, field.getFileName());
		H5 question = new H5(field.getQuestion() + (field.isRequired() ? "*" : ""));
		singleFileUpload.setDropLabel(new Span("Drop files here. Max size: 20MB"));
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
		Div content = new Div(question, singleFileUpload, currentUpload);
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
		Span note = new Span("For this question you must answer using recording");

		if (field.isRequired()) {
			binder.withValidator(step -> step.getFieldValue(field.getId()) != null, "Audio is required");
			recorder.setLabel("Requried");
		}
		VerticalLayout question = new VerticalLayout();
		question.addClassNames(LumoUtility.Padding.NONE);
		question.add(note);
		if (field.isTextToSpeech()) {
			AudioTag audio = new AudioTag(field.getSpeech());
			audio.setWidthFull();
			note.setText(note.getText() + ". " + "Click play to hear the question");
			question.add(audio);

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
