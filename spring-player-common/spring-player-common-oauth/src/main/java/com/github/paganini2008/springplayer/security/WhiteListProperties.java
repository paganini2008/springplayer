
package com.github.paganini2008.springplayer.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * WhiteListProperties
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("!'${security.oauth2.client.white-list}'.isEmpty()")
@ConfigurationProperties(prefix = "security.oauth2.client")
public class WhiteListProperties implements InitializingBean {

	private static final String PATH_VARS_PATTERN = "\\{(.*?)\\}";

	private final WebApplicationContext applicationContext;

	@Getter
	@Setter
	private List<String> whiteListUrls = new ArrayList<>();

	@Override
	public void afterPropertiesSet() {
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> requestMapper = mapping.getHandlerMethods();

		requestMapper.keySet().forEach(info -> {
			HandlerMethod handlerMethod = requestMapper.get(info);

			WhiteList method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), WhiteList.class);
			Optional.ofNullable(method).ifPresent(c -> info.getPatternsCondition().getPatterns()
					.forEach(url -> whiteListUrls.add(url.replaceAll(PATH_VARS_PATTERN, "*"))));

			WhiteList controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), WhiteList.class);
			Optional.ofNullable(controller).ifPresent(c -> info.getPatternsCondition().getPatterns()
					.forEach(url -> whiteListUrls.add(url.replaceAll(PATH_VARS_PATTERN, "*"))));
		});

	}

}
