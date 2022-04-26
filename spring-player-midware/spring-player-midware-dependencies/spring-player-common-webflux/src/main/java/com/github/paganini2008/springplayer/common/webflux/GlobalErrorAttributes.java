package com.github.paganini2008.springplayer.common.webflux;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.github.paganini2008.springplayer.common.ExceptionDescriptor;

/**
 * 
 * GlobalErrorAttributes
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
		Throwable e = this.getError(request);
		if (e instanceof ExceptionDescriptor) {
			errorAttributes.put("errorCode", ((ExceptionDescriptor) e).getErrorCode());
		}
		return errorAttributes;
	}

}
