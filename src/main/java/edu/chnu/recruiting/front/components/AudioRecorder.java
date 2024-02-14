package edu.chnu.recruiting.front.components;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.front.components.microphone.DataReceiver;
import edu.chnu.recruiting.front.components.microphone.VUserMedia;

@Tag("audio-recorder")
public class AudioRecorder extends HorizontalLayout {
	VUserMedia mic;
	Button startRecording = new Button("Start recording");
	Button stopRecording = new Button("Stop recording");
	Button onoff = new Button("OnOff");
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
		add(new HorizontalLayout(onoff, startRecording, stopRecording, mic));

        startRecording.addClickListener(e -> {
            mic.startRecording();
            stopRecording.setEnabled(true);
            startRecording.setEnabled(false);
        });

        stopRecording.setEnabled(false);
        stopRecording.addClickListener(e -> {
            mic.stopRecording();
            stopRecording.setEnabled(false);
            startRecording.setEnabled(true);
        });
        
        onoff.addClickListener(e -> {
            if(mic.isOpen()) {
                mic.closeMicrophone();
                onoff.setText("Open camera");
                startRecording.setEnabled(false);
            } else {
            	mic.openMicrophone();
                onoff.setText("Close camera");
                startRecording.setEnabled(true);
            }
        });
        
		mic.addFinishedListener(e -> {
			fireEvent(new RecordedEvent(this, currentRecording.toByteArray()));
		});
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
