package com.github.paganini2008.springplayer.oauth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.springplayer.common.ApiResult;

import lombok.RequiredArgsConstructor;

/**
 * 
 * SpAuthenticationEntryPoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class SpAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		ApiResult<?> apiResult = ApiResult.failed("认证失败", e.getMessage());
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(apiResult));
	}

}
