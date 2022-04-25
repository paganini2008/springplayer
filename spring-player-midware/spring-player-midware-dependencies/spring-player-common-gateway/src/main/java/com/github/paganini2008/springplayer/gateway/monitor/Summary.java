package com.github.paganini2008.springplayer.gateway.monitor;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 
 * Summary
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class Summary<T extends UserMetric<T>> {

	private final AtomicStampedReference<T> ref = new AtomicStampedReference<T>(null, 0);

	public T merge(T value) {
		T current;
		T update;
		do {
			current = ref.getReference();
			update = merge(current, value);
		} while (!ref.compareAndSet(current, update, ref.getStamp(), ref.getStamp() + 1));
		return update;
	}
	
	public T get() {
		return ref.getReference();
	}

	protected T merge(T current, T update) {
		if (current != null) {
			return current.merge(update);
		}
		return update;
	}

}
