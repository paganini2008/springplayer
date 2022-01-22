
package com.github.paganini2008.springplayer.webmvc;

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
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.filter.GenericFilterBean;

import com.github.paganini2008.devtools.collection.CollectionUtils;

import lombok.SneakyThrows;

/**
 * 
 * RequestHeaderContextHolderFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestHeaderContextHolderFilter extends GenericFilterBean {

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		Map<String, String> headerMap = CollectionUtils.toList(request.getHeaderNames()).stream().collect(Collectors
				.toMap(headerName -> headerName, headerName -> request.getHeader(headerName), (a, b) -> a, LinkedCaseInsensitiveMap::new));
		RequestHeaderContextHolder.setHeaders(headerMap);

		filterChain.doFilter(request, response);
		RequestHeaderContextHolder.clear();
	}

}
