package com.github.paganini2008.springplayer.security;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.i18n.api.RemoteI18nService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * ErrorMessageSource
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class ErrorMessageSource {

	private static final String DEFAULT_PROJECT_NAME = "oauth2";
	private static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage();
	private final RemoteI18nService remoteI18nService;

	public String getErrorMessage(ErrorCode errorCode) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String lang = null;
		if (request != null) {
			lang = request.getHeader("lang");
			if (StringUtils.isBlank(lang)) {
				lang = request.getHeader("Lang");
			}
		}
		if (StringUtils.isBlank(lang)) {
			lang = DEFAULT_LANGUAGE;
		}
		String message = errorCode.getDefaultMessage();
		ApiResult<String> result = remoteI18nService.getMessage(DEFAULT_PROJECT_NAME, lang, errorCode.getMessageKey());
		if (StringUtils.isNotBlank(result.getData())) {
			message = result.getData();
		}
		return message;
	}

}
