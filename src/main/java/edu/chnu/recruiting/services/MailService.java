package edu.chnu.recruiting.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.models.templates.VerifyEmailModel;
import edu.chnu.recruiting.utils.TemplateUtils;
import edu.chnu.recruiting.utils.enums.EmailTemplate;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {

	private static ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private TemplateUtils templateUtils;

	@Value("${spring.mail.from:}")
	private String from;

	public void sendVerificationEmail(VerificationToken token) {
		VerifyEmailModel model = new VerifyEmailModel();
		model.setUsername(token.getUser().getUsername());
		model.setUrl(getBaseUrl() + "/registration-confirm?token=" + token.getToken());
		executorService.submit(() -> {
			this.sendTemplateEmial(token.getUser().getEmail(), EmailTemplate.VERIFY_EMAIL.getSubject(),
					EmailTemplate.VERIFY_EMAIL.getTemplateName(), model);
		});
	}

	public void sendTemplateEmial(String to, String subject, String templateName, Object model) {
		String body = this.templateUtils.processTemplate(templateName, model);
		this.sendEmail(to, subject, body);
	}

	public void sendEmail(String to, String subject, String text) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);

			mailSender.send(message);
			log.info("Successfuly sent '{}' email to '{}'", subject, to);
		} catch (Exception e) {
			log.error("Failed to send '{}' email to '{}'", subject, to, e);
		}
	}

	private String getBaseUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	}
}
