package com.github.paganini2008.springplayer.webmvc;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.github.paganini2008.springplayer.common.ExceptionDescriptor;

/**
 * 
 * GlobalErrorAttributes
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
		Throwable e = this.getError(webRequest);
		if (e instanceof ExceptionDescriptor) {
			errorAttributes.put("errorCode", ((ExceptionDescriptor) e).getErrorCode());
		}
		return errorAttributes;
	}

}
