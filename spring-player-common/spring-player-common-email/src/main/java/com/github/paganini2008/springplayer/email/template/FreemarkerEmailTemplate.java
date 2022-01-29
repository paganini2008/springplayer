package com.github.paganini2008.springplayer.email.template;

import static com.github.paganini2008.devtools.CharsetUtils.UTF_8_NAME;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.paganini2008.devtools.CharsetUtils;
import com.github.paganini2008.devtools.io.FileUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * FreemarkerEmailTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class FreemarkerEmailTemplate implements EmailTemplate {

	public FreemarkerEmailTemplate() {
		this(defaultConfiguration());
	}

	public FreemarkerEmailTemplate(Configuration configuration) {
		this.configuration = configuration;
	}

	private final Configuration configuration;

	@Override
	public String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) throws Exception {
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		configuration.setTemplateLoader(templateLoader);

		Template template = new Template(templateName, templateContent, configuration);
		StringWriter stringWriter = new StringWriter();
		template.process(kwargs, stringWriter);
		return stringWriter.toString();
	}

	private static Configuration defaultConfiguration() {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
		configuration.setNumberFormat("#");
		configuration.setDateFormat("yyyy-MM-dd");
		configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		configuration.setDefaultEncoding(UTF_8_NAME);
		configuration.setURLEscapingCharset(UTF_8_NAME);
		configuration.setLocale(Locale.getDefault());
		return configuration;
	}

	public static void main(String[] args) throws Exception {
		FreemarkerEmailTemplate emailTemplate = new FreemarkerEmailTemplate();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("messageCode", "1");
		dataMap.put("messageStatus", "200");
		dataMap.put("cause", "123");
		dataMap.put("lastModified", new Date());
		String template = FileUtils.readFileToString(new File("d:/work/test.ftl"), CharsetUtils.UTF_8);
		String content = emailTemplate.loadContent("test", template, Collections.singletonMap("params", dataMap));
		System.out.println(content);

	}

}
