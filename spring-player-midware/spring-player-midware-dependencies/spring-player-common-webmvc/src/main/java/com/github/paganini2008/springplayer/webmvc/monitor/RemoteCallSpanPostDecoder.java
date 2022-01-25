package com.github.paganini2008.springplayer.webmvc.monitor;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.webmvc.HttpHeadersContextHolder;
import com.github.paganini2008.springplayer.webmvc.WebUtils;

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
			HttpHeaders httpHeaders = WebUtils.copyHeaders(response);

			Span span = new Span();
			span.setTraceId(httpHeaders.getFirst(REQUEST_HEADER_TRACE_ID));
			span.setSpan(Integer.parseInt(httpHeaders.getFirst(REQUEST_HEADER_SPAN)));
			span.setParentSpan(Integer.parseInt(httpHeaders.getFirst(REQUEST_HEADER_PARENT_SPAN)));
			span.setTimestamp(httpHeaders.getFirst(REQUEST_HEADER_TIMESTAMP));
			span.setPath(apiResult.getRequestPath());
			span.setDownstreamElapsed(apiResult.getElapsed());
			span.setUpstreamElapsed(System.currentTimeMillis() - Long.parseLong(span.getTimestamp()));
			span.setStatus(response.status());

			String trace = objectMapper.writeValueAsString(span);

			String traceHeader = httpHeaders.getFirst(REQUEST_HEADER_TRACE);
			if (StringUtils.isNotBlank(traceHeader)) {
				traceHeader = traceHeader + ";" + trace;
			} else {
				traceHeader = trace;
			}

			HttpHeaders currentHeaders = HttpHeadersContextHolder.get();
			String latestHeader = currentHeaders.getFirst(REQUEST_HEADER_TRACE);
			if (StringUtils.isNotBlank(latestHeader)) {
				latestHeader += ";" + traceHeader;
			} else {
				latestHeader = traceHeader;
			}
			currentHeaders.set(REQUEST_HEADER_TRACE, latestHeader);
			System.out.println("traceHeader: " + HttpHeadersContextHolder.getHeader(REQUEST_HEADER_TRACE));
		}
		return result;
	}

}
