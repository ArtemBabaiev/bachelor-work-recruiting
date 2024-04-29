package edu.chnu.recruiting.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateUtils {
	@Autowired
	private TemplateEngine templateEngine;
	
	public String processTemplate(String template, Object model) {
		Context thymeleafContext = new Context();
	    thymeleafContext.setVariable("model", model);
	    return templateEngine.process(template, thymeleafContext);
	}
}
