package com.github.paganini2008.springplayer.common.monitor;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.devtools.multithreads.Executable;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;

/**
 * 
 * RequestTpsContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class RequestTpsContextHolder implements Executable, InitializingBean, DisposableBean {

	private static final ConcurrentMap<String, TpsUpdater> cache = new ConcurrentHashMap<>();

	private Timer timer;

	public static TpsUpdater get(String repr) {
		return get(repr, () -> new SimpleTpsUpdater());
	}

	public static TpsUpdater get(String repr, Supplier<TpsUpdater> supplier) {
		return MapUtils.get(cache, repr, supplier);
	}

	@Override
	public void destroy() throws Exception {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (timer == null) {
			timer = ThreadUtils.scheduleWithFixedDelay(this, 1, TimeUnit.SECONDS);
		}
	}

	@Override
	public boolean execute() throws Throwable {
		cache.values().forEach(u -> u.set());
		return true;
	}

}
