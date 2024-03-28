package edu.chnu.recruiting.front.views;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.components.AudioRecorder;
import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@PageTitle("tests")
@Route(value = "tests",layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
@Slf4j
public class TestExampleView extends HorizontalLayout {
    
    AudioRecorder recorder = new AudioRecorder();

    public TestExampleView() {
        setMargin(true);

        Button play = new Button(VaadinIcon.PLAY.create(), e -> {
			getElement().executeJs("""
					var msg = new SpeechSynthesisUtterance();
					msg.text = "%s";
					msg.lang = 'en';
					window.speechSynthesis.speak(msg);
					""".formatted("How are you doing?"));
		});
        
        recorder.openMedia();
        
        recorder.addRecordedListener(e -> {        	
        	try {
        		log.info("handling recored");
        		FileOutputStream fs = new FileOutputStream(new File("C:/MyData/test.file"));
        		byte[] bytes = e.getRecording();
        		fs.write(bytes);
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
        });
        
        add(recorder, play);
    }

}

