package com.github.paganini2008.springplayer.crumb.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACES;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;
import static com.github.paganini2008.springplayer.web.HttpRequestContextHolder.getHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.web.HttpRequestContextHolder;

/**
 * 
 * ApiCallUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class ApiCallUtils {

	public static String currentTraceId(HttpServletRequest request) {
		String traceId = request.getHeader(REQUEST_HEADER_TRACE_ID);
		if (StringUtils.isBlank(traceId)) {
			traceId = currentTraceId();
		}
		return traceId;
	}

	public static String currentTraceId() {
		return getHeader(REQUEST_HEADER_TRACE_ID);
	}

	public static String currentSpanId(HttpServletRequest request) {
		String spanId = request.getHeader(REQUEST_HEADER_SPAN_ID);
		if (StringUtils.isBlank(spanId)) {
			spanId = currentSpanId();
		}
		return spanId;
	}

	public static String currentSpanId() {
		return getHeader(REQUEST_HEADER_SPAN_ID);
	}

	public static String currentParentSpanId() {
		return getHeader(REQUEST_HEADER_PARENT_SPAN_ID);
	}

	public static String currentTimestamp(HttpServletRequest request) {
		String timestamp = request.getHeader(REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isBlank(timestamp)) {
			timestamp = currentTimestamp();
		}
		return timestamp;
	}

	public static String currentTimestamp() {
		return getHeader(REQUEST_HEADER_TIMESTAMP);
	}

	public static void setApiHeaders(HttpHeaders httpHeaders) {
		httpHeaders.set(REQUEST_HEADER_TIMESTAMP, currentTimestamp());
		httpHeaders.set(REQUEST_HEADER_TIMESTAMP, currentTimestamp());
		httpHeaders.set(REQUEST_HEADER_TRACE_ID, currentTraceId());
		httpHeaders.set(REQUEST_HEADER_SPAN_ID, currentSpanId());
		httpHeaders.set(REQUEST_HEADER_PARENT_SPAN_ID, currentParentSpanId());
		String traces = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TRACES);
		httpHeaders.set(REQUEST_HEADER_TRACES, traces);
	}

	public static void setApiHeaders(HttpServletResponse response) {
		response.setHeader(REQUEST_HEADER_TIMESTAMP, currentTimestamp());
		response.setHeader(REQUEST_HEADER_TRACE_ID, currentTraceId());
		response.setHeader(REQUEST_HEADER_SPAN_ID, currentSpanId());
		response.setHeader(REQUEST_HEADER_PARENT_SPAN_ID, currentParentSpanId());
		String traces = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TRACES);
		response.setHeader(REQUEST_HEADER_TRACES, traces);
	}

}
