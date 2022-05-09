package com.github.paganini2008.springplayer.common.email.template;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.pegdown.PegDownProcessor;

import com.github.paganini2008.devtools.CharsetUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.io.FileUtils;

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
	
	public static void main(String[] args) throws Exception {
		String template = FileUtils.readFileToString(new File("d:/work/云路基础能力.md"), CharsetUtils.UTF_8);
		MarkdownEmailTemplate emailTemplate = new MarkdownEmailTemplate();
		System.out.println(emailTemplate.loadContent("云路基础能力", template, Collections.singletonMap("author", "冯岩")));
	}

}
