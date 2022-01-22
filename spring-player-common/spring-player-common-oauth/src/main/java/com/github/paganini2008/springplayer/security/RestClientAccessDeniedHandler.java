package com.github.paganini2008.springplayer.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.springplayer.common.ApiResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RestClientAccessDeniedHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestClientAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;
	private final ErrorMessageSource errorMessageSource;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		if (log.isWarnEnabled()) {
			log.warn(e.getMessage(), e);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiResult<String> result = ApiResult.failed(errorMessageSource.getErrorMessage(ErrorCodes.ACCESS_DENIED));
		result.setRequestPath(request.getServletPath());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}

}
