package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.TIMESTAMP;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;

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
			HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
			ApiResult<?> result = (ApiResult<?>) body;
			if (StringUtils.isNotBlank(httpRequest.getHeader(TIMESTAMP))) {
				long timestamp = Long.parseLong(httpRequest.getHeader(TIMESTAMP));
				result.setElapsed(System.currentTimeMillis() - timestamp);
			}
			result.setRequestPath(httpRequest.getServletPath());
		}
		return body;
	}

}
