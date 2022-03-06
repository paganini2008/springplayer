package com.github.paganini2008.springplayer.redis.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import com.github.paganini2008.devtools.CharsetUtils;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.devtools.reflection.FieldUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RedisSharedLock
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class RedisSharedLock implements SharedLock, ApplicationListener<RedisKeyExpiredEvent<?>> {

	public RedisSharedLock(String lockName, RedisConnectionFactory connectionFactory) {
		this(lockName, connectionFactory, 60, 1);
	}

	public RedisSharedLock(String lockName, RedisConnectionFactory connectionFactory, int expiration, int maxPermits) {
		this.connectionFactory = connectionFactory;
		this.expiration = expiration;
		this.maxPermits = maxPermits;
		this.counter = initializeCounter(lockName);
	}

	private final int maxPermits;
	private final RedisConnectionFactory connectionFactory;
	private final int expiration;
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private RedisAtomicInteger counter;
	private long startTime;
	private RedisTemplate<String, Integer> ops;
	private volatile boolean reused = true;
	private volatile boolean expired;

	@SuppressWarnings("unchecked")
	protected RedisAtomicInteger initializeCounter(String lockName) {
		RedisAtomicInteger counter = new RedisAtomicInteger(lockName + ":counter", connectionFactory, 0);
		counter.expire(expiration, TimeUnit.SECONDS);
		ops = (RedisTemplate<String, Integer>) FieldUtils.readField(counter, "generalOps");

		this.expired = false;
		this.startTime = System.currentTimeMillis();
		return counter;
	}

	public boolean isReused() {
		return reused;
	}

	public void setReused(boolean reused) {
		this.reused = reused;
	}

	public boolean isExpired() {
		return expired;
	}

	@Override
	public boolean acquire() {
		while (true) {
			lock.lock();
			try {
				if (!expired && counter.get() < maxPermits) {
					counter.incrementAndGet();
					renewBeforeExpiration();
					return true;
				} else {
					try {
						condition.await(1000L, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						break;
					}
				}
			} catch (RuntimeException e) {
				throw new SharedLockException(e.getMessage(), e);
			} finally {
				lock.unlock();
			}
		}
		renewBeforeExpiration();
		return false;
	}

	@Override
	public boolean acquire(long timeout, TimeUnit timeUnit) {
		final long begin = System.nanoTime();
		long elapsed;
		long nanosTimeout = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
		while (true) {
			lock.lock();
			try {
				if (!expired && counter.get() < maxPermits) {
					counter.incrementAndGet();
					renewBeforeExpiration();
					return true;
				} else {
					if (nanosTimeout > 0) {
						try {
							condition.awaitNanos(nanosTimeout);
						} catch (InterruptedException e) {
							break;
						}
						elapsed = (System.nanoTime() - begin);
						nanosTimeout -= elapsed;
					} else {
						break;
					}
				}
			} catch (RuntimeException e) {
				throw new SharedLockException(e.getMessage(), e);
			} finally {
				lock.unlock();
			}
		}
		renewBeforeExpiration();
		return false;
	}

	private void renewBeforeExpiration() {
		String lockName = getLockName();
		if (ops.hasKey(lockName)) {
			Long ttl = ops.getExpire(lockName, TimeUnit.SECONDS);
			if (ttl != null && ttl.longValue() > 0 && ttl.longValue() < 3) {
				counter.expire(expiration, TimeUnit.SECONDS);
			}
		}
	}

	@Override
	public boolean tryAcquire() {
		try {
			if (counter.get() < maxPermits) {
				counter.incrementAndGet();
				return true;
			}
		} catch (RuntimeException e) {
			throw new SharedLockException(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public long cons() {
		return maxPermits - availablePermits();
	}

	@Override
	public void release() {
		if (!isLocked()) {
			return;
		}
		lock.lock();
		try {
			condition.signalAll();
			if (counter.decrementAndGet() < 0) {
				counter.set(0);
			}
		} catch (RuntimeException e) {
			throw new SharedLockException(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isLocked() {
		try {
			return counter.get() > 0;
		} catch (RuntimeException e) {
			throw new SharedLockException(e.getMessage(), e);
		}
	}

	@Override
	public long join() {
		while (isLocked()) {
			ThreadUtils.randomSleep(1000L);
		}
		return System.currentTimeMillis() - startTime;
	}

	@Override
	public String getLockName() {
		return counter.getKey();
	}

	@Override
	public int getExpiration() {
		return expiration;
	}

	@Override
	public long availablePermits() {
		try {
			return maxPermits - counter.get();
		} catch (RuntimeException e) {
			throw new SharedLockException(e.getMessage(), e);
		}
	}

	@Override
	public void onApplicationEvent(RedisKeyExpiredEvent<?> event) {
		String expiredKey = new String(event.getSource(), CharsetUtils.UTF_8);
		String lockName = getLockName();
		if (!expiredKey.equals(lockName)) {
			return;
		}
		if (log.isInfoEnabled()) {
			log.info("Lock '{}' was expired.", lockName);
		}
		expired = true;
		if (reused) {
			lock.lock();
			try {
				this.counter = initializeCounter(lockName);
				if (log.isInfoEnabled()) {
					log.info("Lock '{}' has been recreated.", lockName);
				}
			} finally {
				lock.unlock();
			}
		}
	}

}
