package com.github.paganini2008.springplayer.common.email.template;

import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

/**
 * 
 * ThymeleafEmailTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class ThymeleafEmailTemplate implements EmailTemplate {
	
    private final TemplateEngine templateEngine;

	@Override
	public String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) throws Exception {
		Context context = new Context();
		context.setVariables(kwargs);
		return templateEngine.process(templateContent, context);
	}

}
