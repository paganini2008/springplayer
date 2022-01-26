package com.github.paganini2008.springplayer.webmvc;

import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.StringUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 
 * Oauth2RequestInterceptor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class Oauth2RequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		String auth = HttpHeadersContextHolder.getHeader("Authorization");
		if (StringUtils.isNotBlank(auth)) {
			template.header("Authorization", auth);
		}
	}

}
