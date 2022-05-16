package com.github.paganini2008.springplayer.common.webmvc;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.github.paganini2008.devtools.CharsetUtils;
import com.github.paganini2008.devtools.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * WebRequestPreHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class WebRequestPreHandler extends RequestBodyAdviceAdapter {

	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		if (log.isTraceEnabled()) {
			try {
				String str = IOUtils.toString(inputMessage.getBody(), CharsetUtils.UTF_8);
				log.trace("RequestBody: {}", str);
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}
		}
		return body;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

}
