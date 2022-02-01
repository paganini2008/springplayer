package com.github.paganini2008.springplayer.crumb;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.web.HttpRequestContextHolder;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;

/**
 * 
 * RemoteCallSpanPostDecoder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class RemoteCallSpanPostDecoder extends ResponseEntityDecoder {

	RemoteCallSpanPostDecoder(Decoder decoder, ObjectMapper objectMapper) {
		super(decoder);
		this.objectMapper = objectMapper;
	}

	private final ObjectMapper objectMapper;

	public Object decode(Response response, Type type) throws IOException, FeignException {
		Object result = super.decode(response, type);
		if (result instanceof ApiResult) {
			ApiResult apiResult = (ApiResult) result;
			HttpHeaders httpHeaders = copyHeaders(response);

			Span span = new Span();
			span.setTraceId(httpHeaders.getFirst(REQUEST_HEADER_TRACE_ID));
			span.setSpan(Integer.parseInt(httpHeaders.getFirst(REQUEST_HEADER_SPAN)));
			span.setParentSpan(Integer.parseInt(httpHeaders.getFirst(REQUEST_HEADER_PARENT_SPAN)));
			span.setTimestamp(httpHeaders.getFirst(REQUEST_HEADER_TIMESTAMP));
			span.setPath(apiResult.getRequestPath());
			span.setElapsed(new long[] { apiResult.getElapsed(), System.currentTimeMillis() - Long.parseLong(span.getTimestamp()) });
			span.setStatus(response.status());

			String trace = objectMapper.writeValueAsString(span);

			String traceHeader = httpHeaders.getFirst(REQUEST_HEADER_TRACE);
			if (StringUtils.isNotBlank(traceHeader)) {
				traceHeader = traceHeader + ";" + trace;
			} else {
				traceHeader = trace;
			}

			String latestTraces = HttpRequestContextHolder.getHeaders().getFirst(REQUEST_HEADER_TRACE);
			if (StringUtils.isNotBlank(latestTraces)) {
				latestTraces += ";" + traceHeader;
			} else {
				latestTraces = traceHeader;
			}
			HttpRequestContextHolder.getHeaders().set(REQUEST_HEADER_TRACE, latestTraces);
		}
		return result;
	}

	private HttpHeaders copyHeaders(Response response) {
		HttpHeaders headers = new HttpHeaders();
		response.headers().entrySet().forEach(e -> {
			headers.addAll(e.getKey(), e.getValue() != null ? new ArrayList<>(e.getValue()) : new ArrayList<>());
		});
		return headers;
	}

}
