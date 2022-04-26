package com.github.paganini2008.springplayer.common.gateway.monitor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.paganini2008.devtools.collection.MapUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * HttpResponseTrace
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class HttpRequestTrace implements HttpTrace, Comparable<HttpRequestTrace> {

	private String id = UUID.randomUUID().toString();
	private String host;
	private String method;
	private String path;
	private String remoteAddr;
	private String localAddr;
	private Map<String, ?> header;
	private Map<String, ?> cookie;
	private String body;
	private Map<String, ?> queryParam;

	@JsonIgnore
	private long timestamp;

	public HttpRequestTrace() {
		this.timestamp = System.currentTimeMillis();
	}

	public HttpRequestTrace(String method, String path) {
		this();
		this.method = method;
		this.path = path;
	}

	public int getBodySize() {
		return body != null ? body.getBytes().length : 0;
	}

	public Map<String, ?> getHeader() {
		if (header == null) {
			return MapUtils.emptyMap();
		}
		return header;
	}

	public Map<String, ?> getCookie() {
		if (cookie == null) {
			return MapUtils.emptyMap();
		}
		return cookie;
	}

	public String getTimeRepr() {
		if (timestamp <= 0) {
			return "";
		}
		return LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault())
				.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public String toString() {
		return getMethod() + " " + getPath();
	}

	public static HttpRequestTrace extractFrom(ServerHttpRequest request) {
		HttpRequestTrace httpTrace = new HttpRequestTrace();
		httpTrace.setId(UUID.randomUUID().toString());
		httpTrace.setHost(request.getURI().getHost());
		httpTrace.setPath(request.getPath().pathWithinApplication().value());
		httpTrace.setMethod(request.getMethodValue().toUpperCase());
		if (request.getRemoteAddress() != null) {
			httpTrace.setRemoteAddr(request.getRemoteAddress().toString());
		}
		if (request.getLocalAddress() != null) {
			httpTrace.setLocalAddr(request.getLocalAddress().toString());
		}
		MultiValueMap<String, ?> multiValueMap = request.getQueryParams();
		if (MapUtils.isNotEmpty(multiValueMap)) {
			httpTrace.setQueryParam(multiValueMap.toSingleValueMap());
		}
		multiValueMap = request.getHeaders();
		if (MapUtils.isNotEmpty(multiValueMap)) {
			httpTrace.setHeader(multiValueMap.toSingleValueMap());
		}
		multiValueMap = request.getCookies();
		if (MapUtils.isNotEmpty(multiValueMap)) {
			httpTrace.setCookie(multiValueMap.toSingleValueMap());
		}
		return httpTrace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof HttpRequestTrace) {
			HttpRequestTrace other = (HttpRequestTrace) obj;
			return getId().equals(other.getId()) && getPath().equals(other.getPath()) && getMethod().equals(other.getMethod());
		}
		return false;
	}

	@Override
	public int compareTo(HttpRequestTrace other) {
		return getId().compareTo(other.getId());
	}

}
