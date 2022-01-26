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
import com.github.paganini2008.springplayer.i18n.I18nUtils;

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

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		if (log.isWarnEnabled()) {
			log.warn(e.getMessage(), e);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		String lang = request.getHeader("lang");
		ApiResult<String> result = ApiResult.failed(I18nUtils.getErrorMessage(lang, ErrorCodes.ACCESS_DENIED));
		result.setRequestPath(request.getServletPath());
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}

}
