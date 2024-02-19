package edu.chnu.recruiting.configuration;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

@Service
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource()
				.addSessionInitListener(initEvent -> System.out.println("A new Session has been initialized!"));
		event.getSource()
		.addSessionDestroyListener(e -> System.out.println("Session has been destryoed!"));
	}

}
