package com.github.paganini2008.springplayer.common.redis.lock;

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
		this.lockName = lockName;
		this.connectionFactory = connectionFactory;
		this.expiration = expiration;
		this.maxPermits = maxPermits;
		this.counter = initializeCounter(lockName);
	}

	private final String lockName;
	private final RedisConnectionFactory connectionFactory;
	private final int expiration;
	private final int maxPermits;
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private RedisAtomicInteger counter;
	private long startTime;
	private RedisTemplate<String, Integer> ops;
	private volatile boolean reused = true;
	private volatile boolean expired;

	public boolean isReused() {
		return reused;
	}

	public void setReused(boolean reused) {
		this.reused = reused;
	}

	public boolean isExpired() {
		return expired;
	}

	private void recreateCounterIfLockNameAbsent() {
		lock.lock();
		try {
			if (!ops.hasKey(counter.getKey())) {
				expired = true;
				if (reused) {
					this.counter = initializeCounter(lockName);
					if (counter.get() != 0) {
						counter.set(0);
					}
					if (log.isInfoEnabled()) {
						log.info("Lock '{}' has been recreated.", lockName);
					}
				} else {
					if (log.isWarnEnabled()) {
						log.warn("Lock '{}' is expired.");
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	protected RedisAtomicInteger initializeCounter(String lockName) {
		RedisAtomicInteger counter = new RedisAtomicInteger(lockName + ":counter", connectionFactory);
		counter.expire(expiration, TimeUnit.SECONDS);
		this.ops = (RedisTemplate<String, Integer>) FieldUtils.readField(counter, "generalOps");
		this.expired = false;
		this.startTime = System.currentTimeMillis();
		return counter;
	}

	@Override
	public boolean acquire() {
		boolean taken = false;
		while (true) {
			lock.lock();
			try {
				if (taken = (!expired && hasLock() && counter.get() < maxPermits)) {
					counter.incrementAndGet();
					return true;
				} else {
					try {
						condition.await(1000L, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						break;
					}
				}
			} finally {
				lock.unlock();
				if (taken) {
					renewBeforeExpiration();
				}
			}
		}
		return false;
	}

	private boolean hasLock() {
		try {
			if (ops.hasKey(counter.getKey())) {
				return true;
			}
			recreateCounterIfLockNameAbsent();
			return ops.hasKey(counter.getKey());
		} catch (RuntimeException e) {
			return false;
		}
	}

	@Override
	public boolean acquire(long timeout, TimeUnit timeUnit) {
		final long begin = System.nanoTime();
		long elapsed;
		long nanosTimeout = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
		boolean taken = false;
		while (true) {
			lock.lock();
			try {
				if (taken = (!expired && hasLock() && counter.get() < maxPermits)) {
					counter.incrementAndGet();
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
			} finally {
				lock.unlock();
				if (taken) {
					renewBeforeExpiration();
				}
			}
		}
		return false;
	}

	private void renewBeforeExpiration() {
		lock.lock();
		try {
			String key = counter.getKey();
			if (ops.hasKey(key)) {
				Long ttl = ops.getExpire(key, TimeUnit.SECONDS);
				if (ttl != null && ttl.longValue() > 0 && ttl.longValue() <= 5) {
					counter.expire(expiration, TimeUnit.SECONDS);
					if (log.isTraceEnabled()) {
						log.trace("Renew the lock counter before its expiration.");
					}
				}
			}
		} finally {
			lock.unlock();
		}

	}

	@Override
	public boolean tryAcquire() {
		boolean taken = false;
		lock.lock();
		try {
			if (taken = (!expired && hasLock() && counter.get() < maxPermits)) {
				counter.incrementAndGet();
				return true;
			}
		} finally {
			lock.unlock();
			if (taken) {
				renewBeforeExpiration();
			}
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
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isLocked() {
		while (true) {
			lock.lock();
			try {
				if (!expired && hasLock()) {
					return counter.get() > 0;
				}
			} finally {
				lock.unlock();
			}
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
		return lockName;
	}

	@Override
	public int getExpiration() {
		return expiration;
	}

	@Override
	public long availablePermits() {
		while (true) {
			lock.lock();
			try {
				if (!expired && hasLock()) {
					return maxPermits - counter.get();
				}
			} finally {
				lock.unlock();
			}
		}
	}

	@Override
	public void onApplicationEvent(RedisKeyExpiredEvent<?> event) {
		String expiredKey = new String(event.getSource(), CharsetUtils.UTF_8);
		if (expiredKey.equals(counter.getKey())) {
			recreateCounterIfLockNameAbsent();
		}
	}
}
