package com.github.paganini2008.springplayer.crumb.webmvc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.springplayer.crumb.ConcurrencyUpdater;
import com.github.paganini2008.springplayer.crumb.SimpleConcurrencyUpdater;

/**
 * 
 * RequestConcurrencyHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RequestConcurrencyHolder {

	private static final ConcurrentMap<String, ConcurrencyUpdater> cache = new ConcurrentHashMap<>();

	public static ConcurrencyUpdater get(String repr) {
		return get(repr, () -> new SimpleConcurrencyUpdater());
	}

	public static ConcurrencyUpdater get(String repr, Supplier<ConcurrencyUpdater> supplier) {
		return MapUtils.get(cache, repr, supplier);
	}

}
