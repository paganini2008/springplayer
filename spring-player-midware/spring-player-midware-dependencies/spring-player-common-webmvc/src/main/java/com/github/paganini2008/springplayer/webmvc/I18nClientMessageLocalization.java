package com.github.paganini2008.springplayer.webmvc;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.i18n.I18nUtils;

/**
 * 
 * I18nClientMessageLocalization
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class I18nClientMessageLocalization implements MessageLocalization {

	@Value("${spring.application.name}")
	private String applicationName;

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		String text = I18nUtils.getMessage(applicationName, locale.getLanguage(), code);
		if (StringUtils.isBlank(text)) {
			text = defaultMessage;
		}
		if (ArrayUtils.isNotEmpty(args)) {
			text = MessageFormat.format(text, args);
		}
		return text;
	}

}
