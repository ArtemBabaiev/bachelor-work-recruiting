package edu.chnu.recruiting.front.views.application;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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

import edu.chnu.recruiting.front.components.AudioRecorder;
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

		nextBtn.addClickListener(e -> fireEvent(new NextEvent(this, binder.getBean())));
		backBtn.addClickListener(e -> fireEvent(new BackEvent(this, binder.getBean())));
		backBtn.setEnabled(model.getId() > 0);
		backBtn.setVisible(model.getId() > 0);

		add(new H2("Step " + (model.getId() + 1) + ": " + model.getName()));

		for (var field : model.getFields()) {
			configureField(field);
		}
		binder.validate();
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
		Span question = new Span(field.getQuestion() + (field.isRequired() ? "*" : ""));
		singleFileUpload.addSucceededListener(e -> {
			InputStream fileData = memoryBuffer.getInputStream();
			try {
				field.setUserValue(Base64.getEncoder().encodeToString(fileData.readAllBytes()));
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
		// 100 MebiBytes
		singleFileUpload.setMaxFileSize(104_857_600);
		Div content = new Div(question, singleFileUpload, currentUpload);
		if (field.isRequired()) {
			binder.withValidator(step -> step.getFieldValue(field.getId()) != null, "File upload is required");
		}
		return content;
	}

	private Component getAudio(WizardField field) {
		AudioRecorder recorder = new AudioRecorder();
		recorder.openMedia();
		recorder.addRecordedListener(e -> {		
			field.setUserValue(Base64.getEncoder().encodeToString(e.getRecording()));
			binder.validate();
			UI.getCurrent().push();
		});

		if (field.isRequired()) {
			binder.withValidator(step -> step.getFieldValue(field.getId()) != null, "Audio is required");
		}
		Div question = new Div();
		if (field.isTextToSpeech()) {
			Button play = new Button("Play question", VaadinIcon.PLAY.create(), e -> {
				getElement().executeJs("""
						var msg = new SpeechSynthesisUtterance();
						msg.text = "%s";
						msg.lang = 'en';
						window.speechSynthesis.speak(msg);
						""".formatted(field.getQuestion()));
			});
			Button noSound = new Button("Cant hear question?");
			noSound.addClickListener(e -> {
				question.remove(play);
				question.add(new Span(field.getQuestion()));
				noSound.setEnabled(false);
				noSound.setVisible(false);
			});
			question.add(play, noSound);

		} else {
			question.add(new Span(field.getQuestion()));
		}
		return new Div(question, recorder);
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
