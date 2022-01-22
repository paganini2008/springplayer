package com.github.paganini2008.springplayer.security;

import static com.github.paganini2008.springplayer.common.Constants.TIMESTAMP;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;

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
@Component
@RequiredArgsConstructor
public class FailureAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;
	private final ErrorMessageSource errorMessageSource;

	@Value("${spring.application.name}")
	private String applicationName;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
		if (log.isWarnEnabled()) {
			log.warn(e.getMessage(), e);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiResult<String> result = ApiResult.failed(e.getMessage(), e.getMessage());
		result.setRequestPath(request.getServletPath());
		if (StringUtils.isNotBlank(request.getHeader(TIMESTAMP))) {
			long timestamp = Long.parseLong(request.getHeader(TIMESTAMP));
			result.setElapsed(System.currentTimeMillis() - timestamp);
		}
		ErrorCode errorCode = ErrorCodes.matches(e);
		result.setMsg(errorMessageSource.getErrorMessage(errorCode));

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}

}
