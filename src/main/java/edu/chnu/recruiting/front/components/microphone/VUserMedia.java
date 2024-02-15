package edu.chnu.recruiting.front.components.microphone;

import java.io.OutputStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamReceiver;
import com.vaadin.flow.server.StreamVariable;
import com.vaadin.flow.shared.Registration;

/**
 * A special video element that streams content from browser camera.
 * <p>During streaming, users can record still or video clips of the stream, that browser send to the server. The data can be accessed using the DataReceiver interface, see {@link #setReceiver(DataReceiver)}.</p>
 */
@Tag("v-microphone")
public class VUserMedia extends Component {

    private boolean isOn;
    private boolean recording;

    public VUserMedia() {
        getElement().setProperty("volume", 0);
    }

    public void setReceiver(DataReceiver receiver) {
        getElement().setAttribute("target", new StreamReceiver(
                getElement().getNode(), "microphone", new AudioStreamVariable(receiver)));
    }

    private void fireFinishedEvent(String mime) {
        fireEvent(new FinishedEvent(this, true, mime));
    }

    public void startRecording() {
        if(!isOn) {
            throw new IllegalStateException("Media is not on");
        }
        recording = true;
        getElement().executeJs("""
                let target = this.getAttribute("target");;
                this.recorder = new MediaRecorder(this.stream);
                this.recorder.ondataavailable = e => {
                    let formData = new FormData();
                    formData.append("data", e.data);
                    fetch(target, {
                        method: "post",
                        body: formData
                    }).then(response => console.log(response));
                }
                this.recorder.start();
                    """);
    }

    public void stopRecording() {
        if(!recording) {
            throw new IllegalStateException("Not recording");
        }
        getElement().executeJs("this.recorder.stop()");
        recording = false;
    }

    public void closeMicrophone() {
        isOn = false;
        getElement().executeJs("""
                if(this.stream!=null) {
                    this.stream.getTracks().forEach( t=> {
                        t.stop();
                    });
                    this.stream = null;
                }
                """);
    }



    public void openMicrophone() {
        openMedia("{audio:true}");
    }
    
    public void openCamera() {
        openMedia("{audio:true,video:true}");
    }

    public void openMedia(String optionsJson) {
        isOn = true;
        getElement().executeJs("""
                if(this.stream == null) {
                    if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                        navigator.mediaDevices.getUserMedia(%s).then(stream => {
                            this.stream = stream;
                            this.srcObject = this.stream;
                        });
                    }
                }
                        """.formatted(optionsJson));
    }

    public boolean isOpen() {
        return isOn;
    }

    public Registration addFinishedListener(ComponentEventListener<FinishedEvent> listener) {
        return addListener(FinishedEvent.class, listener);
    }


    private class AudioStreamVariable implements StreamVariable {

        String mime;
        DataReceiver receiver;

        public AudioStreamVariable(DataReceiver receiver) {
            this.receiver = receiver;
        }


        @Override
        public OutputStream getOutputStream() {
            return receiver.getOutputStream();
        }

        @Override
        public boolean isInterrupted() {
            return false;
        }

        @Override
        public boolean listenProgress() {
            return false;
        }

        @Override
        public void onProgress(StreamingProgressEvent arg0) {

        }

        @Override
        public void streamingFailed(StreamingErrorEvent arg0) {

        }

        @Override
        public void streamingFinished(StreamingEndEvent arg0) {
            fireFinishedEvent(mime);

        }

        @Override
        public void streamingStarted(StreamingStartEvent arg0) {
            mime = arg0.getMimeType();
        }

    }
    
    public static class FinishedEvent extends ComponentEvent<VUserMedia>{

        private String mime;

        public FinishedEvent(VUserMedia source, boolean fromClient, String mime) {
            super(source, fromClient);
            this.mime =mime;
        }

        public String getMime() {
            return mime;
        }

    }

}
