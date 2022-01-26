package com.github.paganini2008.springplayer.webmvc.monitor;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;
import static com.github.paganini2008.springplayer.webmvc.HttpHeadersContextHolder.getHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.webmvc.HttpHeadersContextHolder;

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

	public static String currentSpan(HttpServletRequest request) {
		String spanId = request.getHeader(REQUEST_HEADER_SPAN);
		if (StringUtils.isBlank(spanId)) {
			spanId = currentSpan();
		}
		return spanId;
	}

	public static String currentSpan() {
		return getHeader(REQUEST_HEADER_SPAN);
	}

	public static String currentParentSpan() {
		return getHeader(REQUEST_HEADER_PARENT_SPAN);
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
		httpHeaders.set(REQUEST_HEADER_SPAN, currentSpan());
		httpHeaders.set(REQUEST_HEADER_PARENT_SPAN, currentParentSpan());
		String traces = HttpHeadersContextHolder.getHeader(REQUEST_HEADER_TRACE);
		httpHeaders.set(REQUEST_HEADER_TRACE, traces);
	}

	public static void setApiHeaders(HttpServletResponse response) {
		response.setHeader(REQUEST_HEADER_TIMESTAMP, currentTimestamp());
		response.setHeader(REQUEST_HEADER_TRACE_ID, currentTraceId());
		response.setHeader(REQUEST_HEADER_SPAN, currentSpan());
		response.setHeader(REQUEST_HEADER_PARENT_SPAN, currentParentSpan());
		String traces = HttpHeadersContextHolder.getHeader(REQUEST_HEADER_TRACE);
		response.setHeader(REQUEST_HEADER_TRACE, traces);
	}

}
