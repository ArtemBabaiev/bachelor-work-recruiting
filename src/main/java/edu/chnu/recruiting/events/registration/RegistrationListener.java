package edu.chnu.recruiting.events.registration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import edu.chnu.recruiting.OnRegistrationCompleteEvent;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.services.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private VerificationTokenService service;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Value("${base-url}")
	private String contextPath;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = contextPath + "/registration-confirm?token=" + token;
		String anchor = "<a href='" + confirmationUrl + "'>Confirm url</a>";
		String message = "Registration successful. Please confirm email\r\n" + anchor;

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message);
		mailSender.send(email);
	}
}
