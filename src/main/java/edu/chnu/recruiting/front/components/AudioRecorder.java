package edu.chnu.recruiting.front.components;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.front.components.microphone.DataReceiver;
import edu.chnu.recruiting.front.components.microphone.VUserMedia;

@Tag("audio-recorder")
public class AudioRecorder extends HorizontalLayout {
	VUserMedia mic;
	Button startRecording = new Button(new Icon(VaadinIcon.CIRCLE));
	Button stopRecording = new Button(new Icon(VaadinIcon.STOP));
	ByteArrayOutputStream currentRecording;
	boolean recordingInProcess = false;

	public AudioRecorder() {
		mic = new VUserMedia();
		mic.setReceiver(new DataReceiver() {
			@Override
			public OutputStream getOutputStream() {
				currentRecording = new ByteArrayOutputStream();
				return currentRecording;
			}
		});
		add(new HorizontalLayout(startRecording, stopRecording, mic));

		this.makeButtonActive(false, startRecording, stopRecording);

		startRecording.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);

		stopRecording.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

		startRecording.addClickListener(e -> {
			mic.startRecording(300_000); // 5 minutes max recording time

			this.makeButtonActive(false, startRecording);
			this.makeButtonActive(true, stopRecording);
		});

		stopRecording.setEnabled(false);
		stopRecording.addClickListener(e -> {
			mic.stopRecording();
			this.makeButtonActive(true, startRecording);
			this.makeButtonActive(false, stopRecording);
		});


		mic.addFinishedListener(e -> {
			fireEvent(new RecordedEvent(this, currentRecording.toByteArray()));
		});
	}

	public void openMedia() {
		if (!mic.isOpen()) {
			mic.openMicrophone();
			this.makeButtonActive(true, startRecording);
		}
	}
	
	public void closeMedia() {
		if (mic.isOpen()) {
			mic.closeMedia();
			this.makeButtonActive(false, startRecording);
		}
	}

	private void makeButtonActive(boolean value, Button... buttons) {
		for (Button button : buttons) {
			button.setVisible(value);
			button.setEnabled(value);
		}
	}

	public Registration addRecordedListener(ComponentEventListener<RecordedEvent> listener) {
		return addListener(RecordedEvent.class, listener);
	}

	public static class RecordedEvent extends ComponentEvent<AudioRecorder> {

		private byte[] recording;

		public RecordedEvent(AudioRecorder source, byte[] recording) {
			super(source, false);
			this.recording = recording;
		}

		public byte[] getRecording() {
			return this.recording;
		}

	}
}
