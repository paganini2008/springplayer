
package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.TENANT_ID;

import java.util.ArrayList;
import java.util.Arrays;
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
 * HttpHeadersContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpHeadersContextHolder extends GenericFilterBean {

	private static final ThreadLocal<HttpHeaders> headersThreadLocal = TransmittableThreadLocal.withInitial(() -> new HttpHeaders());

	public static String getHeader(String headerName) {
		return getHeader(headerName, "");
	}

	public static String getHeader(String headerName, String defaultValue) {
		HttpHeaders httpHeaders = get();
		String headerValue = httpHeaders.getFirst(headerName);
		if (StringUtils.isBlank(headerValue)) {
			headerValue = defaultValue;
		}
		return headerValue;
	}

	public static List<String> getHeaders(String headerName) {
		HttpHeaders httpHeaders = get();
		List<String> headerValues = httpHeaders.get(headerName);
		if (headerValues == null) {
			headerValues = new ArrayList<>();
		}
		return headerValues;
	}

	public static synchronized void addHeader(String headerName, String headerValue) {
		HttpHeaders httpHeaders = get();
		httpHeaders.add(headerName, headerValue);
	}

	public static synchronized void addHeaders(String headerName, List<String> headerValues) {
		HttpHeaders httpHeaders = get();
		httpHeaders.addAll(headerName, headerValues);
	}

	public static synchronized void addHeaders(MultiValueMap<String, String> headerMap) {
		HttpHeaders httpHeaders = get();
		httpHeaders.addAll(headerMap);
	}

	public static synchronized void addHeaderIfAbsent(String headerName, String headerValue) {
		addHeaderIfAbsent(headerName, () -> headerValue);
	}

	public static synchronized void addHeaderIfAbsent(String headerName, Supplier<String> headerValue) {
		HttpHeaders httpHeaders = get();
		if (!httpHeaders.containsKey(headerName)) {
			httpHeaders.add(headerName, headerValue.get());
		}
	}

	public static synchronized void setHeader(String headerName, String headerValue) {
		HttpHeaders httpHeaders = get();
		httpHeaders.set(headerName, headerValue);
	}

	public static synchronized void setHeaders(Map<String, String> headerMap) {
		HttpHeaders httpHeaders = get();
		httpHeaders.setAll(headerMap);
	}

	public static HttpHeaders get() {
		return headersThreadLocal.get();
	}

	public static void clear() {
		headersThreadLocal.remove();
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

		headersThreadLocal.set(httpHeaders);

		filterChain.doFilter(request, response);
		clear();
	}

	public static void main(String[] args) {
		HttpHeaders httpHeaders = HttpHeadersContextHolder.get();
		httpHeaders.addAll("a", Arrays.asList("1", "2", "3"));
		System.out.println(httpHeaders);
		httpHeaders.addAll("a", Arrays.asList("4", "5", "6"));
		System.out.println(httpHeaders);

		List<String> list = httpHeaders.get("a");
		list.add("1000");
		httpHeaders.replace("a", list);
		System.out.println(HttpHeadersContextHolder.get());
	}

}
