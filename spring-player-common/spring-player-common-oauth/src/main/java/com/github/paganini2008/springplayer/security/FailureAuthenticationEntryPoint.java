package com.github.paganini2008.springplayer.security;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.LocaleUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.i18n.I18nUtils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * FailureAuthenticationEntryPoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class FailureAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
		if (log.isWarnEnabled()) {
			log.warn(e.getMessage(), e);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiResult<String> result = ApiResult.failed(e.getMessage());
		result.setRequestPath(request.getServletPath());
		if (StringUtils.isNotBlank(request.getHeader(REQUEST_HEADER_TIMESTAMP))) {
			long timestamp = Long.parseLong(request.getHeader(REQUEST_HEADER_TIMESTAMP));
			result.setElapsed(System.currentTimeMillis() - timestamp);
		}
		ErrorCode errorCode = ErrorCodes.matches(e);
		String msg = getI18nMessage(request, errorCode);
		result.setMsg(msg);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}

	private String getI18nMessage(HttpServletRequest request, ErrorCode errorCode) {
		String lang = request.getHeader("lang");
		Locale locale;
		if (StringUtils.isNotBlank(lang)) {
			locale = LocaleUtils.getLocale(lang);
		} else {
			locale = LocaleContextHolder.getLocale();
		}
		return I18nUtils.getErrorMessage(locale.getLanguage(), errorCode);
	}

}
