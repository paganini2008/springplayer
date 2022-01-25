package com.github.paganini2008.springplayer.webmvc.monitor;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.filter.GenericFilterBean;

import com.github.paganini2008.springplayer.webmvc.HttpHeadersContextHolder;

import lombok.RequiredArgsConstructor;

/**
 * 
 * CallSpanPreIterceptor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class CallSpanPreIterceptor extends GenericFilterBean {

	private final RedisAtomicLong spanGen;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpHeadersContextHolder.addHeaderIfAbsent(REQUEST_HEADER_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		HttpHeadersContextHolder.addHeaderIfAbsent(REQUEST_HEADER_TRACE_ID, UUID.randomUUID().toString());
		HttpHeadersContextHolder.addHeaderIfAbsent(REQUEST_HEADER_PARENT_SPAN, () -> String.valueOf(spanGen.getAndIncrement()));
		HttpHeadersContextHolder.addHeaderIfAbsent(REQUEST_HEADER_SPAN, () -> String.valueOf(spanGen.getAndIncrement()));
		chain.doFilter(request, response);
	}



}
