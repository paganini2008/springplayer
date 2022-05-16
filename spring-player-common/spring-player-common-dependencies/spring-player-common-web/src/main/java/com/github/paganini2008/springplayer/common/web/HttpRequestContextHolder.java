
package com.github.paganini2008.springplayer.common.web;

import static com.github.paganini2008.springplayer.common.Constants.TENANT_ID;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.GenericFilterBean;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.github.paganini2008.devtools.StringUtils;

import lombok.SneakyThrows;

/**
 * 
 * HttpRequestContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpRequestContextHolder extends GenericFilterBean {

	private static final ThreadLocal<HttpRequestInfo> ttl = TransmittableThreadLocal.withInitial(() -> new HttpRequestInfo());

	public static String getHeader(String headerName) {
		return getHeader(headerName, "");
	}

	public static String getHeader(String headerName, String defaultValue) {
		HttpHeaders httpHeaders = getHeaders();
		if (httpHeaders == null) {
			return defaultValue;
		}
		String headerValue = httpHeaders.getFirst(headerName);
		if (StringUtils.isBlank(headerValue)) {
			headerValue = defaultValue;
		}
		return headerValue;
	}

	public static List<String> getHeaders(String headerName) {
		HttpHeaders httpHeaders = getHeaders();
		return httpHeaders.getOrDefault(headerName, Collections.emptyList());
	}

	public static void addHeader(String headerName, String headerValue) {
		HttpHeaders httpHeaders = getHeaders();
		httpHeaders.add(headerName, headerValue);
	}

	public static void addHeaders(String headerName, List<String> headerValues) {
		HttpHeaders httpHeaders = getHeaders();
		httpHeaders.addAll(headerName, headerValues);
	}

	public static void addHeaders(MultiValueMap<String, String> headerMap) {
		HttpHeaders httpHeaders = getHeaders();
		httpHeaders.addAll(headerMap);
	}

	public static void addHeaderIfAbsent(String headerName, String headerValue) {
		addHeaderIfAbsent(headerName, () -> headerValue);
	}

	public static void addHeaderIfAbsent(String headerName, Supplier<String> headerValue) {
		HttpHeaders httpHeaders = getHeaders();
		if (!httpHeaders.containsKey(headerName)) {
			httpHeaders.add(headerName, headerValue.get());
		}
	}

	public static void setHeader(String headerName, String headerValue) {
		HttpHeaders httpHeaders = getHeaders();
		httpHeaders.set(headerName, headerValue);
	}

	public static void setHeaders(Map<String, String> headerMap) {
		HttpHeaders httpHeaders = getHeaders();
		httpHeaders.setAll(headerMap);
	}

	public static HttpRequestInfo get() {
		HttpRequestInfo info = ttl.get();
		if (info == null) {
			info = new HttpRequestInfo();
		}
		return info;
	}

	public static HttpHeaders getHeaders() {
		return get().getHeaders();
	}

	public static String getPath() {
		return get().getPath();
	}

	public static void clear() {
		ttl.remove();
	}

	public static Long getTenantId() {
		return Long.valueOf(getHeader(TENANT_ID, "1"));
	}

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		HttpHeaders httpHeaders = WebUtils.copyHeaders(request);
		ttl.set(new HttpRequestInfo(request.getMethod(), request.getServletPath(), httpHeaders));

		filterChain.doFilter(request, response);
		clear();
	}

}
