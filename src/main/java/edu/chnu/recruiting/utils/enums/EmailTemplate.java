package edu.chnu.recruiting.utils.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {
	VERIFY_EMAIL("verify_email", "Verify your email");
	
	private EmailTemplate(String templateName, String subject) {
		this.subject = subject;
		this.templateName = templateName;
	}
	
	private String templateName;
	private String subject;
}
