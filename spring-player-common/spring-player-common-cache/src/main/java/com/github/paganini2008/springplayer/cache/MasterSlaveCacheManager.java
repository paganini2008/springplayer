package com.github.paganini2008.springplayer.cache;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.github.paganini2008.devtools.collection.CollectionUtils;

/**
 * 
 * MasterSlaveCacheManager
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MasterSlaveCacheManager implements CacheManager {

	private final CacheManager master;
	private final CacheManager slave;

	public MasterSlaveCacheManager(CacheManager master, CacheManager slave) {
		this.master = master;
		this.slave = slave;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = master.getCache(name);
		if (cache == null) {
			cache = slave.getCache(name);
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		Collection<String> names = master.getCacheNames();
		if (CollectionUtils.isEmpty(names)) {
			names = slave.getCacheNames();
		}
		return names;
	}

}
