package com.github.paganini2008.springplayer.gateway.monitor;

import java.time.Instant;
import java.util.List;

import com.github.paganini2008.devtools.collection.ConcurrentSortedBoundedList;
import com.github.paganini2008.devtools.collection.ConcurrentSortedBoundedMap;
import com.github.paganini2008.devtools.time.AppendableTimeSlotMap;

/**
 * 
 * AppendableTimeWindowMap
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class AppendableTimeWindowMap<T> extends AppendableTimeSlotMap<T> {

	private static final long serialVersionUID = -7410648463089768948L;

	public AppendableTimeWindowMap(int span, TimeWindowUnit timeWindowUnit, int bufferSize, int maxListSize) {
		super(new ConcurrentSortedBoundedMap<>(Integer.min(bufferSize, timeWindowUnit.sizeOf(span, 1))), span,
				timeWindowUnit.getTimeSlot());
		this.timeWindowUnit = timeWindowUnit;
		this.maxListSize = maxListSize;
	}

	private final TimeWindowUnit timeWindowUnit;
	private final int maxListSize;

	public TimeWindowUnit getTimeWindowUnit() {
		return timeWindowUnit;
	}

	public int getMaxListSize() {
		return maxListSize;
	}

	@Override
	public List<T> append(Instant key, T value) {
		return super.append(key, value, () -> new ConcurrentSortedBoundedList<>(maxListSize));
	}

	@Override
	public List<T> appendAll(Instant key, List<T> list) {
		return super.appendAll(key, list, () -> new ConcurrentSortedBoundedList<>(maxListSize));
	}

}
