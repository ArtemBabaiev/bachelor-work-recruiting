package edu.chnu.recruiting.configuration;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import edu.chnu.recruiting.front.exceptionHandlers.CustomErrorHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource()
				.addSessionInitListener(initEvent -> sessionInitHandler(initEvent));
		event.getSource()
		.addSessionDestroyListener(destroyEvent -> log.info("Session has been destryoed!"));
	}

	private void sessionInitHandler(SessionInitEvent initEvent) {
		initEvent.getSession().setErrorHandler(new CustomErrorHandler());
	}

}
