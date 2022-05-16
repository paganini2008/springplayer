package com.github.paganini2008.springplayer.common.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import com.github.paganini2008.devtools.collection.MapUtils;

/**
 * 
 * RequestConcurrencyContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RequestConcurrencyContextHolder {

	private static final ConcurrentMap<String, ConcurrencyUpdater> cache = new ConcurrentHashMap<>();

	public static ConcurrencyUpdater get(String repr) {
		return get(repr, () -> new SimpleConcurrencyUpdater());
	}

	public static ConcurrencyUpdater get(String repr, Supplier<ConcurrencyUpdater> supplier) {
		return MapUtils.get(cache, repr, supplier);
	}

}
