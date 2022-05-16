package com.github.paganini2008.springplayer.common.oauth;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.LocaleUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.context.MessageLocalization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GlobalAccessDeniedHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;
	private final MessageLocalization messageLocalization;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		if (log.isWarnEnabled()) {
			log.warn(e.getMessage(), e);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		String msg = getI18nMessage(request, ErrorCodes.ACCESS_DENIED);
		ApiResult<String> result = ApiResult.failed(msg);
		result.setRequestPath(request.getServletPath());
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}

	private String getI18nMessage(HttpServletRequest request, ErrorCode errorCode) {
		String lang = request.getHeader("lang");
		Locale locale;
		if (StringUtils.isNotBlank(lang)) {
			locale = LocaleUtils.getLocale(lang);
		} else {
			locale = LocaleContextHolder.getLocale();
		}
		return messageLocalization.getMessage(errorCode, locale);
	}

}
