package edu.chnu.recruiting.front.views;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.components.AudioRecorder;
import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Audio")
@Route(value = "audio",layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class AudioExampleView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;
    
    AudioRecorder recorder = new AudioRecorder();

    public AudioExampleView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        recorder.addRecordedListener(e -> {        	
        	try {
        		FileOutputStream fs = new FileOutputStream(new File("C:/MyData/test.file"));
        		byte[] bytes = e.getRecording();
        		fs.write(bytes);
				var encoded = Base64.getEncoder().encodeToString(bytes);
				System.out.println(encoded);
                //System.out.println(Base64.getDecoder().decode(bytes, bytes));
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
        });
        
        add(name, sayHello, recorder);
    }

}

