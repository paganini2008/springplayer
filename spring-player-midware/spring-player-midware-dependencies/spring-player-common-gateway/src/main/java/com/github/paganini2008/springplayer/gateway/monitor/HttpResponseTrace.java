package com.github.paganini2008.springplayer.gateway.monitor;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * HttpResponseTrace
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Getter
@Setter
public class HttpResponseTrace implements HttpTrace, Comparable<HttpResponseTrace> {

	private HttpRequestTrace request;
	private Map<String, ?> header;
	private Map<String, ?> cookie;
	private HttpStatus status;
	private int concurrents;

	@JsonIgnore
	private long timestamp;

	public HttpResponseTrace() {
		this.timestamp = System.currentTimeMillis();
	}

	public HttpResponseTrace(HttpRequestTrace request, HttpStatus status, int concurrents) {
		this();
		this.request = request;
		this.status = status;
		this.concurrents = concurrents;
	}

	public long getElapsed() {
		return getTimestamp() - request.getTimestamp();
	}

	public boolean is1xx() {
		return status.is1xxInformational();
	}

	public boolean is2xx() {
		return status.is2xxSuccessful();
	}

	public boolean is3xx() {
		return status.is3xxRedirection();
	}

	public boolean is4xx() {
		return status.is4xxClientError();
	}

	public boolean is5xx() {
		return status.is5xxServerError();
	}

	public boolean isOk() {
		return !status.isError();
	}

	public int getCost() {
		int cost = getStatus().series().value() * 100 + (int) getElapsed() + getConcurrents();
		if (!isOk()) {
			cost += 500000;
		}
		if (getElapsed() >= MAX_REQUEST_TIMEOUT) {
			cost += 300000;
		}
		if (getConcurrents() >= MAX_CONCURRENCY) {
			cost += 200000;
		}
		return cost;
	}

	@Override
	public String getPath() {
		return getRequest().getPath();
	}

	@Override
	public String getMethod() {
		return getRequest().getMethod();
	}

	@Override
	public String toString() {
		return getRequest().toString() + " " + getStatus() + " cost: " + getCost();
	}

	public void printPrettyLog(Logger log) {
		if (log.isTraceEnabled()) {
			log.trace("# Http Trace Info Start");
			log.trace("[       id]: {}", request.getId());
			log.trace("[     host]: {}", request.getHost());
			log.trace("[     path]: {}", request.getMethod() + " " + request.getPath());
			log.trace("[    param]: {}", request.getQueryParam());
			log.trace("[  address]: {}/{}", request.getRemoteAddr(), request.getLocalAddr());
			log.trace("[   header]: {}", request.getHeader());
			log.trace("[body size]: {}", request.getBodySize());
			log.trace("[   status]: {}", getStatus());
			log.trace("[  elapsed]: {}", getElapsed());
			log.trace("[   header]: {}", getHeader());
			log.trace("# Http Trace Info End");
		}
	}

	@Override
	public int compareTo(HttpResponseTrace other) {
		int lcost = getCost();
		int rcost = other.getCost();
		if (lcost == rcost) {
			return getRequest().compareTo(other.getRequest());
		}
		return rcost - lcost;
	}

}
