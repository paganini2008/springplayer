package com.github.paganini2008.springplayer.monitor.crumb;

import org.springframework.http.HttpStatus;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ThresholdTraceStore
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class ThresholdTraceStore implements TraceStore {

	private final HttpStatus.Series[] httpStatus;
	private final long maxRequestTime;
	private final int maxConucrrents;

	public ThresholdTraceStore() {
		this(null, null, null);
	}

	public ThresholdTraceStore(HttpStatus.Series[] httpStatus, Long maxRequestTime, Long maxConucrrents) {
		this.httpStatus = httpStatus;
		this.maxRequestTime = maxRequestTime != null ? maxRequestTime.longValue() : 60000L;
		this.maxConucrrents = maxConucrrents != null ? maxConucrrents.intValue() : 200;
	}

	@Override
	public boolean shouldTrace(Span span) {
		boolean result = false;
		if (ArrayUtils.isNotEmpty(httpStatus)) {
			result |= ArrayUtils.contains(httpStatus, HttpStatus.Series.valueOf(span.getStatus()));
		} else {
			result |= HttpStatus.valueOf(span.getStatus()).isError();
		}
		result |= span.getElapsed() >= maxRequestTime;
		result |= span.getConcurrents() >= maxConucrrents;
		return result;
	}

	@Override
	public void trace(SpanTree spanTree) {
		log.info(JacksonUtils.toJsonString(spanTree));
	}

}
