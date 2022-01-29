package com.github.paganini2008.springplayer.email;

import static com.github.paganini2008.devtools.CharsetUtils.UTF_8_NAME;

import java.util.Locale;
import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

import freemarker.template.Configuration;

/**
 * 
 * EmailSenderConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ MailProperties.class, ThymeleafProperties.class, FreeMarkerProperties.class })
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

	@ConditionalOnMissingBean(name = "markdownEmailTemplate")
	@Bean
	public EmailTemplate markdownEmailTemplate() {
		return new MarkdownEmailTemplate();
	}

	@ConditionalOnMissingBean(name = "freemarkerConfiguration")
	@Bean
	public Configuration freemarkerConfiguration(FreeMarkerProperties freeMarkerProperties) {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
		configuration.setNumberFormat("#");
		configuration.setDateFormat("yyyy-MM-dd");
		configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		configuration.setDefaultEncoding(UTF_8_NAME);
		configuration.setURLEscapingCharset(UTF_8_NAME);
		configuration.setLocale(Locale.getDefault());
		return configuration;
	}

	@ConditionalOnMissingBean(name = "freemarkerEmailTemplate")
	@Bean
	public EmailTemplate freemarkerEmailTemplate(Configuration configuration) {
		return new FreemarkerEmailTemplate(configuration);
	}

	@ConditionalOnMissingBean(name = "thymeleafConfiguration")
	@Bean
	public TemplateEngine thymeleafConfiguration(ThymeleafProperties properties) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(properties.isEnableSpringElCompiler());
		engine.setRenderHiddenMarkersBeforeCheckboxes(properties.isRenderHiddenMarkersBeforeCheckboxes());
		StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
		stringTemplateResolver.setCacheable(false);
		stringTemplateResolver.setTemplateMode(TemplateMode.HTML);
		engine.setTemplateResolver(stringTemplateResolver);
		return engine;
	}

	@ConditionalOnMissingBean(name = "thymeleafEmailTemplate")
	@Bean
	public EmailTemplate thymeleafEmailTemplate(TemplateEngine templateEngine) {
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
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.debug", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "false");
		javaMailProperties.setProperty("mail.smtp.starttls.required", "false");
		javaMailProperties.setProperty("mail.smtp.ssl.enable", "true");
		javaMailProperties.setProperty("mail.imap.ssl.socketFactory.fallback", "false");
		javaMailProperties.setProperty("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		javaMailProperties.setProperty("mail.smtp.timeout", "60000");
		javaMailSender.setJavaMailProperties(javaMailProperties);
		return javaMailSender;
	}

}
