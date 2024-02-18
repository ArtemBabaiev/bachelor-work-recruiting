package edu.chnu.recruiting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.utils.constants.EmailTemplates;

@Service
public class MailService {
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Value("${base-url}")
	private String contextPath;
	
	
	public void sendVerificationEmail(String to, String token) {
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
		mailSender.send(email);
	}
}
