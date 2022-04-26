package com.github.paganini2008.springplayer.common.context;

import java.text.MessageFormat;
import java.util.Locale;

import com.github.paganini2008.devtools.ArrayUtils;

/**
 * 
 * NoopMessageLocalization
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public class NoopMessageLocalization implements MessageLocalization {

	@Override
	public String getMessage(String code, Locale locale, Object[] args, String defaultMessage) {
		if (ArrayUtils.isNotEmpty(args)) {
			return MessageFormat.format(defaultMessage, args);
		}
		return defaultMessage;
	}

}
