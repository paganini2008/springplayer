package com.github.paganini2008.springplayer.messenger.service;

import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.springplayer.email.JavaEmailService;
import com.github.paganini2008.springplayer.email.RichTextEmail;
import com.github.paganini2008.springplayer.email.TextEmail;
import com.github.paganini2008.springplayer.messenger.entity.EmailEntity;

import lombok.SneakyThrows;

/**
 * 
 * EmailSenderService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class EmailSenderService {

	@Autowired
	private JavaEmailService javaEmailService;

	private final Semaphore lock = new Semaphore(8);

	@SneakyThrows
	public void processSendingEmail(EmailEntity entity) {
		lock.acquire();
		try {
			if (entity.getType() != null) {
				javaEmailService.sendRichTextEmail(convert2RichTextEmail(entity));
			} else {
				javaEmailService.sendTextEmail(convert2TextEmail(entity));
			}
		} finally {
			lock.release();
		}
	}

	private RichTextEmail convert2RichTextEmail(EmailEntity entity) {
		RichTextEmail textEmail = new RichTextEmail();
		BeanUtils.copyProperties(entity, textEmail);
		textEmail.setRichTextType(entity.getType());
		return textEmail;
	}

	private TextEmail convert2TextEmail(EmailEntity entity) {
		TextEmail textEmail = new TextEmail();
		BeanUtils.copyProperties(entity, textEmail);
		return textEmail;
	}
}
