package com.github.paganini2008.springplayer.webflux;

import java.util.List;

import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.collection.LruList;

/**
 * 
 * ApiExceptionContext
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Component
public class ApiExceptionContext {

	private final List<ThrowableInfo> exceptionTraces = new LruList<>(256);

	public List<ThrowableInfo> getExceptionTraces() {
		return exceptionTraces;
	}
	
}
