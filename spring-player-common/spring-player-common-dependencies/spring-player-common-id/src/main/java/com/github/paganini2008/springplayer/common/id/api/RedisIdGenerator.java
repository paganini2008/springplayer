package com.github.paganini2008.springplayer.common.id.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.github.paganini2008.devtools.collection.LruMap;
import com.github.paganini2008.springplayer.common.id.IdGenerator;

/**
 * 
 * RedisIdGenerator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisIdGenerator implements IdGenerator {

	private static final String defaultDatePattern = "yyyyMMddHHmmss";
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(defaultDatePattern);
	private static final int maxConcurrency = 100000;

	private final String keyPrefix;
	private final RedisConnectionFactory connectionFactory;
	private final Map<String, ThreadSafeSupplier> cache;

	public RedisIdGenerator(RedisConnectionFactory connectionFactory) {
		this("id:", connectionFactory);
	}

	public RedisIdGenerator(String keyPrefix, RedisConnectionFactory connectionFactory) {
		this.keyPrefix = keyPrefix;
		this.connectionFactory = connectionFactory;
		this.cache = new LruMap<String, ThreadSafeSupplier>(2);
	}

	private volatile long latestId;

	@Override
	public long currentId() {
		return latestId;
	}

	@Override
	public long generateId() {
		final String timestamp = LocalDateTime.now().format(dtf);
		Supplier<RedisAtomicLong> counter = cache.get(timestamp);
		if (counter == null) {
			cache.putIfAbsent(timestamp, new ThreadSafeSupplier(() -> {
				String counterName = keyPrefix + ":" + timestamp;
				RedisAtomicLong l = new RedisAtomicLong(counterName, connectionFactory);
				l.expire(60, TimeUnit.SECONDS);
				return l;
			}));
			counter = cache.get(timestamp);
		}
		long ticket = counter.get().incrementAndGet();
		if (ticket > maxConcurrency) {
			throw new IllegalStateException("Exceeding maximum concurrency: " + maxConcurrency);
		}
		return (latestId = Long.parseLong(timestamp) * maxConcurrency + ticket);
	}

	/**
	 * 
	 * ThreadSafeSupplier
	 *
	 * @author Fred Feng
	 * @version 1.0.0
	 */
	static class ThreadSafeSupplier implements Supplier<RedisAtomicLong> {

		private final Supplier<RedisAtomicLong> supplier;

		ThreadSafeSupplier(Supplier<RedisAtomicLong> supplier) {
			this.supplier = supplier;
		}

		private volatile RedisAtomicLong counter;

		@Override
		public RedisAtomicLong get() {
			RedisAtomicLong result = counter;
			if (result == null) {
				synchronized (this) {
					result = counter;
					if (result == null) {
						result = supplier.get();
						counter = result;
					}
				}
			}
			return result;
		}

	}

}
