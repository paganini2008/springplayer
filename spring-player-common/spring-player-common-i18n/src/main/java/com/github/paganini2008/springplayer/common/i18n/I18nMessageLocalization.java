package com.github.paganini2008.springplayer.common.i18n;

import java.util.Locale;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.context.MessageLocalization;

/**
 * 
 * I18nMessageLocalization
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public class I18nMessageLocalization implements MessageLocalization {

	@Override
	public String getMessage(String code, Locale locale, Object[] args, String defaultMessage) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		String message = I18nUtils.getMessage(locale.getLanguage(), code);
		if (StringUtils.isBlank(message)) {
			message = defaultMessage;
		}
		return message;
	}

}
