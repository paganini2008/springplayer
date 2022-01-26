package com.github.paganini2008.springplayer.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.webmvc.monitor.ApiCallUtils;

/**
 * 
 * ClientResponsePreHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestControllerAdvice
public class ClientResponsePreHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body instanceof ApiResult) {
			ApiResult<?> result = (ApiResult<?>) body;
			HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
			String timestamp = ApiCallUtils.currentTimestamp(httpRequest);
			if (StringUtils.isNotBlank(timestamp)) {
				result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
			}
			result.setRequestPath(httpRequest.getServletPath());
		}
		HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
		ApiCallUtils.setApiHeaders(httpResponse);
		return body;
	}

}
