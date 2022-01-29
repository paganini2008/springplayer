package com.github.paganini2008.springplayer.email;

import java.net.URL;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.springplayer.email.template.EmailTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SpringEmailService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class SpringEmailService implements JavaEmailService {

	@Qualifier("textEmailTemplate")
	@Autowired
	private EmailTemplate textEmailTemplate;

	@Qualifier("htmlEmailTemplate")
	@Autowired
	private EmailTemplate htmlEmailTemplate;

	@Qualifier("markdownEmailTemplate")
	@Autowired
	private EmailTemplate markdownEmailTemplate;

	@Qualifier("freemarkerEmailTemplate")
	@Autowired
	private EmailTemplate freemarkerEmailTemplate;

	@Qualifier("thymeleafEmailTemplate")
	@Autowired
	private EmailTemplate thymeleafEmailTemplate;

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendTextEmail(TextEmail textEmail) throws Exception {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(textEmail.getFrom());
		simpleMailMessage.setTo(textEmail.getTo());
		if (StringUtils.isNotBlank(textEmail.getReplyTo())) {
			simpleMailMessage.setReplyTo(textEmail.getReplyTo());
		}
		if (ArrayUtils.isNotEmpty(textEmail.getCc())) {
			simpleMailMessage.setCc(textEmail.getCc());
		}
		if (ArrayUtils.isNotEmpty(textEmail.getBcc())) {
			simpleMailMessage.setBcc(textEmail.getBcc());
		}
		simpleMailMessage.setSubject(textEmail.getSubject());
		String text = textEmailTemplate.loadContent(textEmail.getSubject(), textEmail.getTemplate(), textEmail.getVariables());
		simpleMailMessage.setText(text);
		simpleMailMessage.setSentDate(new Date());
		javaMailSender.send(simpleMailMessage);
		log.info("Send mail to: {}", ArrayUtils.join(textEmail.getTo()));
	}

	public void sendRichTextEmail(RichTextEmail textEmail) throws Exception {
		MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
		mimeMessageHelper.setFrom(textEmail.getFrom());
		mimeMessageHelper.setTo(textEmail.getTo());
		if (StringUtils.isNotBlank(textEmail.getReplyTo())) {
			mimeMessageHelper.setReplyTo(textEmail.getReplyTo());
		}
		if (ArrayUtils.isNotEmpty(textEmail.getCc())) {
			mimeMessageHelper.setCc(textEmail.getCc());
		}
		if (ArrayUtils.isNotEmpty(textEmail.getBcc())) {
			mimeMessageHelper.setBcc(textEmail.getBcc());
		}
		mimeMessageHelper.setSubject(textEmail.getSubject());

		String text = "";
		switch (textEmail.getRichTextType()) {
		case HTML:
			text = htmlEmailTemplate.loadContent(textEmail.getSubject(), textEmail.getTemplate(), textEmail.getVariables());
			break;
		case MARKDOWN:
			text = markdownEmailTemplate.loadContent(textEmail.getSubject(), textEmail.getTemplate(), textEmail.getVariables());
			break;
		case FREEMARKER:
			text = freemarkerEmailTemplate.loadContent(textEmail.getSubject(), textEmail.getTemplate(), textEmail.getVariables());
			break;
		case THYMELEAF:
			text = thymeleafEmailTemplate.loadContent(textEmail.getSubject(), textEmail.getTemplate(), textEmail.getVariables());
			break;
		}

		if (MapUtils.isNotEmpty(textEmail.getAttachments())) {
			textEmail.getAttachments().entrySet().forEach(e -> {
				try {
					mimeMessageHelper.addAttachment(e.getKey(), new UrlResource(new URL(e.getValue())));
				} catch (Exception ignored) {
				}
			});
		}

		mimeMessageHelper.setSentDate(new Date());
		mimeMessageHelper.setText(text, true);
		javaMailSender.send(mimeMailMessage);
		log.info("Send mail to: {}", ArrayUtils.join(textEmail.getTo()));
	}

}
