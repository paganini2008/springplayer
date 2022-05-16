package com.github.paganini2008.springplayer.common.webmvc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.web.HttpRequestContextHolder;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 
 * Oauth2RequestInterceptor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnClass({ Feign.class })
@Component
public class Oauth2RequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		String auth = HttpRequestContextHolder.getHeader("Authorization");
		if (StringUtils.isNotBlank(auth)) {
			template.header("Authorization", auth);
		}
	}

}
