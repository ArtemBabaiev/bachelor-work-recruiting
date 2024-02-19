package edu.chnu.recruiting.events.registration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.services.MailService;
import edu.chnu.recruiting.services.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private VerificationTokenService service;

	@Autowired
	private MailService mailService;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		mailService.sendVerificationEmail(user.getEmail(), token);
	}
}
