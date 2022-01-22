package com.github.paganini2008.springplayer.webmvc;

import java.util.Locale;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ApplicationContextUtils;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.i18n.api.RemoteI18nService;

/**
 * 
 * I18nUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class I18nUtils {

	private static final String BEAN_NAME = "remoteI18nService.FeignClientSpecification";
	private static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage();

	public static String getErrorMessage(ErrorCode errorCode) {
		String msg = getMessage(errorCode.getMessageKey());
		if (StringUtils.isBlank(msg)) {
			msg = errorCode.getDefaultMessage();
		}
		return msg;
	}

	public static String getMessage(String code) {
		String applicationName = ApplicationContextUtils.getRequiredProperty("spring.application.name");
		String lang = RequestHeaderContextHolder.getHeader("lang", DEFAULT_LANGUAGE);
		return getMessage(applicationName, lang, code);
	}

	public static String getMessage(String project, String lang, String code) {
		RemoteI18nService remoteI18nService = ApplicationContextUtils.getBean(BEAN_NAME);
		ApiResult<String> result = remoteI18nService.getMessage(project, lang, code);
		return result.getData();
	}

}
