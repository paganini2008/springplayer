package com.github.paganini2008.springplayer.id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.github.paganini2008.devtools.collection.LruMap;

/**
 * 
 * RedisGlobalIdGenerator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisGlobalIdGenerator implements IdGenerator {

	private static final String defaultDatePattern = "yyyyMMddHHmmss";
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(defaultDatePattern);
	private static final int maxConcurrency = 100000;

	private final String keyPrefix;
	private final RedisConnectionFactory connectionFactory;
	private final Map<String, Supplier<RedisAtomicLong>> cache;

	public RedisGlobalIdGenerator(RedisConnectionFactory connectionFactory) {
		this("id:", connectionFactory);
	}

	public RedisGlobalIdGenerator(String keyPrefix, RedisConnectionFactory connectionFactory) {
		this.keyPrefix = keyPrefix;
		this.connectionFactory = connectionFactory;
		this.cache = new LruMap<String, Supplier<RedisAtomicLong>>(10);
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
			cache.putIfAbsent(timestamp, () -> {
				String coutnerName = keyPrefix + ":" + timestamp;
				RedisAtomicLong l = new RedisAtomicLong(coutnerName, connectionFactory);
				l.expire(60, TimeUnit.SECONDS);
				return l;
			});
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
	 * CounterSupplier
	 *
	 * @author Feng Yan
	 * @version 1.0.0
	 */
	class CounterSupplier implements Supplier<RedisAtomicLong> {

		private final Supplier<RedisAtomicLong> supplier;

		CounterSupplier(Supplier<RedisAtomicLong> supplier) {
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
