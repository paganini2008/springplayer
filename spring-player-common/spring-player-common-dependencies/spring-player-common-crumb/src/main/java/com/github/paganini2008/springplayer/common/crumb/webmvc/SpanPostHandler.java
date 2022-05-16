package com.github.paganini2008.springplayer.common.crumb.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACES;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.monitor.ConcurrencyUpdater;
import com.github.paganini2008.springplayer.common.monitor.RequestConcurrencyContextHolder;
import com.github.paganini2008.springplayer.common.monitor.crumb.Span;
import com.github.paganini2008.springplayer.common.web.HttpRequestContextHolder;

import lombok.RequiredArgsConstructor;

/**
 * 
 * SpanPostHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class SpanPostHandler implements ResponseBodyAdvice<Object> {

	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
		HttpHeaders httpHeaders = HttpRequestContextHolder.getHeaders();
		if (httpHeaders == null || StringUtils.isBlank(httpHeaders.getFirst(REQUEST_HEADER_TRACE_ID))) {
			return body;
		}
		String traceId = httpHeaders.getFirst(REQUEST_HEADER_TRACE_ID);
		String timestamp = httpHeaders.getFirst(REQUEST_HEADER_TIMESTAMP);
		long spanId = Long.parseLong(httpHeaders.getFirst(REQUEST_HEADER_SPAN_ID));
		String path = (body instanceof ApiResult) ? ((ApiResult<?>) body).getRequestPath() : httpRequest.getServletPath();
		long elapsed = (body instanceof ApiResult) ? ((ApiResult<?>) body).getElapsed()
				: System.currentTimeMillis() - Long.parseLong(timestamp);
		Span span = new Span(traceId, spanId);
		span.setParentSpanId(Long.parseLong(httpHeaders.getFirst(REQUEST_HEADER_PARENT_SPAN_ID)));
		span.setTimestamp(Long.parseLong(httpHeaders.getFirst(REQUEST_HEADER_TIMESTAMP)));
		span.setPath(path);
		span.setElapsed(elapsed);
		span.setStatus(httpResponse.getStatus());

		ConcurrencyUpdater concurrencyUpdater = RequestConcurrencyContextHolder.get(path);
		span.setConcurrents(concurrencyUpdater.get());

		String trace;
		try {
			trace = objectMapper.writeValueAsString(span);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		String latestTraces = HttpRequestContextHolder.getHeaders().getFirst(REQUEST_HEADER_TRACES);
		if (StringUtils.isNotBlank(latestTraces)) {
			latestTraces += ";" + trace;
		} else {
			latestTraces = trace;
		}
		HttpRequestContextHolder.getHeaders().set(REQUEST_HEADER_TRACES, latestTraces);
		ApiCallUtils.setApiHeaders(httpResponse);
		return body;
	}

}
