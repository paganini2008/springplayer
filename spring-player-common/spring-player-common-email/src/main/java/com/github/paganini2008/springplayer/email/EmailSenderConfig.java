package com.github.paganini2008.springplayer.email;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import com.github.paganini2008.springplayer.email.template.EmailTemplate;
import com.github.paganini2008.springplayer.email.template.FreemarkerEmailTemplate;
import com.github.paganini2008.springplayer.email.template.HtmlEmailTemplate;
import com.github.paganini2008.springplayer.email.template.MarkdownEmailTemplate;
import com.github.paganini2008.springplayer.email.template.TextEmailTemplate;
import com.github.paganini2008.springplayer.email.template.ThymeleafEmailTemplate;

/**
 * 
 * EmailSenderConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class EmailSenderConfig {

	@ConditionalOnMissingBean(name = "textEmailTemplate")
	@Bean
	public EmailTemplate textEmailTemplate() {
		return new TextEmailTemplate();
	}

	@ConditionalOnMissingBean(name = "htmlEmailTemplate")
	@Bean
	public EmailTemplate htmlEmailTemplate() {
		return new HtmlEmailTemplate();
	}

	@ConditionalOnMissingBean(name = "mdEmailTemplate")
	@Bean
	public EmailTemplate mdEmailTemplate() {
		return new MarkdownEmailTemplate();
	}

	@ConditionalOnMissingBean(name = "ftlEmailTemplate")
	@Bean
	public EmailTemplate ftlEmailTemplate() {
		return new FreemarkerEmailTemplate();
	}

	@Bean
	public TemplateEngine templateEngine(ThymeleafProperties properties) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(properties.isEnableSpringElCompiler());
		engine.setRenderHiddenMarkersBeforeCheckboxes(properties.isRenderHiddenMarkersBeforeCheckboxes());
		StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
		stringTemplateResolver.setCacheable(true);
		stringTemplateResolver.setTemplateMode(TemplateMode.HTML);
		engine.setTemplateResolver(stringTemplateResolver);
		return engine;
	}

	@ConditionalOnMissingBean(name = "thfEmailTemplate")
	@Bean
	public EmailTemplate thfEmailTemplate(TemplateEngine templateEngine) {
		return new ThymeleafEmailTemplate(templateEngine);
	}

	@ConditionalOnMissingBean
	@Bean
	public JavaEmailService springEmailService() {
		return new SpringEmailService();
	}

	@Bean
	public JavaMailSender jobMailSender(MailProperties config) {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(config.getHost());
		javaMailSender.setUsername(config.getUsername());
		javaMailSender.setPassword(config.getPassword());
		javaMailSender.setDefaultEncoding(config.getDefaultEncoding().name());
		Properties javaMailProperties = new Properties();
		javaMailProperties.putAll(config.getProperties());
		javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.starttls.enable", false);
		javaMailProperties.put("mail.smtp.starttls.required", false);
		javaMailProperties.put("mail.smtp.timeout", 60000);
		javaMailSender.setJavaMailProperties(javaMailProperties);
		return javaMailSender;
	}

}
