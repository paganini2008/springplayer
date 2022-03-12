package com.github.paganini2008.springplayer.common.monitor.crumb;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_PATH;

import org.slf4j.MDC;
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
		MDC.put(REQUEST_HEADER_TRACE_ID, spanTree.getSpan().getTraceId());
		MDC.put(REQUEST_HEADER_SPAN_ID, String.valueOf(spanTree.getSpan().getSpanId()));
		MDC.put(REQUEST_PATH, spanTree.getSpan().getPath());
		log.info(JacksonUtils.toJsonString(spanTree));
	}

}
