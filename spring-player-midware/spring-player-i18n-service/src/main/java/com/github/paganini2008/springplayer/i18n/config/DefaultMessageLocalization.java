package com.github.paganini2008.springplayer.i18n.config;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.i18n.service.I18nMessageService;
import com.github.paganini2008.springplayer.webmvc.MessageLocalization;

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
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
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