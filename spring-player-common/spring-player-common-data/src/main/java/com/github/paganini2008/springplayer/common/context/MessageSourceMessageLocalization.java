package com.github.paganini2008.springplayer.common.context;

import java.util.Locale;

import org.springframework.context.MessageSource;

import lombok.RequiredArgsConstructor;

/**
 * 
 * MessageSourceMessageLocalization
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class MessageSourceMessageLocalization implements MessageLocalization {

	private final MessageSource messageSource;

	@Override
	public String getMessage(String code, Locale locale, Object[] args, String defaultMessage) {
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

}
