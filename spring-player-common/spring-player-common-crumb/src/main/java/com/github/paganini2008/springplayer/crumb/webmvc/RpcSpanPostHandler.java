package com.github.paganini2008.springplayer.crumb.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACES;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.web.HttpRequestContextHolder;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;

/**
 * 
 * RpcSpanPostHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class RpcSpanPostHandler extends ResponseEntityDecoder {

	RpcSpanPostHandler(Decoder decoder, ObjectMapper objectMapper) {
		super(decoder);
		this.objectMapper = objectMapper;
	}

	private final ObjectMapper objectMapper;

	public Object decode(Response response, Type type) throws IOException, FeignException {
		Object answer = super.decode(response, type);
		if (answer instanceof ApiResult) {
			ApiResult result = (ApiResult) answer;
			HttpHeaders httpHeaders = copyHeaders(response);
			String trace = httpHeaders.getFirst(REQUEST_HEADER_TRACES);
			if (StringUtils.isNotBlank(trace)) {
				String latestTraces = HttpRequestContextHolder.getHeaders().getFirst(REQUEST_HEADER_TRACES);
				if (StringUtils.isNotBlank(latestTraces)) {
					latestTraces += ";" + trace;
				} else {
					latestTraces = trace;
				}
				HttpRequestContextHolder.getHeaders().set(REQUEST_HEADER_TRACES, latestTraces);
			}
		}
		return answer;
	}

	private HttpHeaders copyHeaders(Response response) {
		HttpHeaders headers = new HttpHeaders();
		response.headers().entrySet().forEach(e -> {
			headers.addAll(e.getKey(), e.getValue() != null ? new ArrayList<>(e.getValue()) : new ArrayList<>());
		});
		return headers;
	}

}
