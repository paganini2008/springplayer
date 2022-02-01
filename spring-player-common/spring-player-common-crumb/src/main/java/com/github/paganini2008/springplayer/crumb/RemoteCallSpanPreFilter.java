package com.github.paganini2008.springplayer.crumb;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

/**
 * 
 * RemoteCallSpanPreFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class RemoteCallSpanPreFilter implements RequestInterceptor {
	
	private final RedisAtomicLong spanGen;

	@Override
	public void apply(RequestTemplate template) {
		template.header(REQUEST_HEADER_TRACE_ID, ApiCallUtils.currentTraceId());
		String spanId = ApiCallUtils.currentSpan();
		String nextSpanId = String.valueOf(spanGen.incrementAndGet());
		template.header(REQUEST_HEADER_PARENT_SPAN, spanId);
		template.header(REQUEST_HEADER_SPAN, nextSpanId);
		template.header(REQUEST_HEADER_TIMESTAMP, ApiCallUtils.currentTimestamp());
	}

}
