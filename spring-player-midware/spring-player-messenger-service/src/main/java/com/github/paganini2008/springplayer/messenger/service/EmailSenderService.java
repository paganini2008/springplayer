package com.github.paganini2008.springplayer.messenger.service;

import java.util.concurrent.Semaphore;

import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.springplayer.common.messenger.model.EmailDTO;
import com.github.paganini2008.springplayer.email.JavaEmailService;
import com.github.paganini2008.springplayer.email.RichTextEmail;
import com.github.paganini2008.springplayer.email.RichTextType;
import com.github.paganini2008.springplayer.email.TextEmail;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * EmailSenderService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@AllArgsConstructor
public class EmailSenderService {

	private final JavaEmailService javaEmailService;

	private final Semaphore lock = new Semaphore(8);

	@SneakyThrows
	public void processSendingEmail(EmailDTO entity) {
		lock.acquire();
		try {
			if (entity.getTemplateType() != null) {
				javaEmailService.sendRichTextEmail(convert2RichTextEmail(entity));
			} else {
				javaEmailService.sendTextEmail(convert2TextEmail(entity));
			}
		} finally {
			lock.release();
		}
	}

	private RichTextEmail convert2RichTextEmail(EmailDTO entity) {
		RichTextEmail textEmail = new RichTextEmail();
		BeanUtils.copyProperties(entity, textEmail);
		textEmail.setRichTextType(RichTextType.valueOf(entity.getTemplateType()));
		return textEmail;
	}

	private TextEmail convert2TextEmail(EmailDTO entity) {
		TextEmail textEmail = new TextEmail();
		BeanUtils.copyProperties(entity, textEmail);
		return textEmail;
	}
}
