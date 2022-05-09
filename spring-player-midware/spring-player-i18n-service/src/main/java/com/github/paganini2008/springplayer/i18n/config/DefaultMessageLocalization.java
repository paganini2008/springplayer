package com.github.paganini2008.springplayer.i18n.config;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.context.MessageLocalization;
import com.github.paganini2008.springplayer.i18n.service.I18nMessageService;

/**
 * 
 * DefaultMessageLocalization
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Primary
@Component
public class DefaultMessageLocalization implements MessageLocalization {

	@Value("${spring.application.name}")
	private String applicationName;

	@Autowired
	private I18nMessageService i18nMessageService;

	@Override
	public String getMessage(String code, Locale locale, Object[] args, String defaultMessage) {
		if (locale == null) {
			locale = LocaleContextHolder.getLocale();
		}
		String text = i18nMessageService.getMessage(applicationName, locale.getLanguage(), code);
		if (StringUtils.isBlank(text)) {
			text = defaultMessage;
		}
		if (ArrayUtils.isNotEmpty(args)) {
			text = MessageFormat.format(text, args);
		}
		return text;
	}

}
