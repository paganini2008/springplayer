package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_PATH;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.paganini2008.springplayer.web.HttpRequestContextHolder;

/**
 * 
 * LoggingWebMvcConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnProperty(name = "springplayer.logging.enabled", matchIfMissing = true)
@Configuration
public class LoggingWebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(traceHandlerInterceptor()).addPathPatterns("/**");
	}

	@Bean
	public HandlerInterceptor traceHandlerInterceptor() {
		return new TraceHandlerInterceptor();
	}

	public static class TraceHandlerInterceptor implements HandlerInterceptor {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			MDC.put(REQUEST_HEADER_TRACE_ID, HttpRequestContextHolder.getHeader(REQUEST_HEADER_TRACE_ID));
			MDC.put(REQUEST_HEADER_SPAN_ID, HttpRequestContextHolder.getHeader(REQUEST_HEADER_SPAN_ID));
			MDC.put(REQUEST_PATH, request.getServletPath());
			return true;
		}

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
				throws Exception {
			MDC.clear();
		}

	}

}
