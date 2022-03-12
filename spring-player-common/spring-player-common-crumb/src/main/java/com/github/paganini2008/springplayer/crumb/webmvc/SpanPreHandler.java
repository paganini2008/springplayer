package com.github.paganini2008.springplayer.crumb.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.github.paganini2008.springplayer.common.monitor.ConcurrencyUpdater;
import com.github.paganini2008.springplayer.common.monitor.RequestConcurrencyContextHolder;
import com.github.paganini2008.springplayer.common.web.HttpRequestContextHolder;

import lombok.RequiredArgsConstructor;

/**
 * 
 * SpanPreHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class SpanPreHandler extends GenericFilterBean {

	private final RedisAtomicLong spanGen;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpRequestContextHolder.addHeaderIfAbsent(REQUEST_HEADER_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		HttpRequestContextHolder.addHeaderIfAbsent(REQUEST_HEADER_TRACE_ID, UUID.randomUUID().toString());
		HttpRequestContextHolder.addHeaderIfAbsent(REQUEST_HEADER_PARENT_SPAN_ID, () -> String.valueOf(spanGen.getAndIncrement()));
		HttpRequestContextHolder.addHeaderIfAbsent(REQUEST_HEADER_SPAN_ID, () -> String.valueOf(spanGen.getAndIncrement()));

		ConcurrencyUpdater concurrencyUpdater = RequestConcurrencyContextHolder.get(((HttpServletRequest) request).getServletPath());
		concurrencyUpdater.increment();

		chain.doFilter(request, response);

		concurrencyUpdater = RequestConcurrencyContextHolder.get(((HttpServletRequest) request).getServletPath());
		concurrencyUpdater.decrement();

	}

}
