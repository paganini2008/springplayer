
package com.github.paganini2008.springplayer.oauth;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.github.paganini2008.devtools.collection.CollectionUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		Map<String, String> headerMap = CollectionUtils.toList(request.getHeaderNames()).stream()
				.collect(Collectors.toConcurrentMap(headerName -> headerName, headerName -> request.getHeader(headerName)));
		TenantContextHolder.setHeaders(headerMap);

		filterChain.doFilter(request, response);
		TenantContextHolder.clear();
	}

}
