package edu.chnu.recruiting.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.chnu.recruiting.utils.constants.EmailTemplates;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Value("${spring.mail.from:}")
	private String from;
	

	public void sendVerificationEmail(String to, String token) {
		String contextPath = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		String subject = EmailTemplates.Subjects.CONFIRMATION;
		String confirmationUrl = contextPath + "/registration-confirm?token=" + token;
		String message = EmailTemplates.Bodies.CONFIRMATION.formatted(confirmationUrl);

		this.sendEmail(to, subject, message);
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(to);
		email.setSubject(subject);
		email.setText(body);
		email.setFrom(from);
		log.info("Sending email to {}", to);
		mailSender.send(email);
		log.info("Email was sent to {}", to);
	}
}
