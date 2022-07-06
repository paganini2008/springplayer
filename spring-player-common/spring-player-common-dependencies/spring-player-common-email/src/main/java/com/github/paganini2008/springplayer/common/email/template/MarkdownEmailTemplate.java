package com.github.paganini2008.springplayer.common.email.template;

import java.util.Map;

import org.pegdown.PegDownProcessor;

import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * MarkdownEmailTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MarkdownEmailTemplate implements EmailTemplate {

	@Override
	public String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) throws Exception {
		String filledTemplateContent = StringUtils.parseText(templateContent, "${", "}", kwargs);
		PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
		String htmlContent = pdp.markdownToHtml(filledTemplateContent);
		return htmlContent;
	}

}
