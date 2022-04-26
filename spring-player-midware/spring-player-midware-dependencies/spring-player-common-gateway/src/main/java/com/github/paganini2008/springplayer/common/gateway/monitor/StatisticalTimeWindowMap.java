package com.github.paganini2008.springplayer.common.gateway.monitor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

import com.github.paganini2008.devtools.RandomDateUtils;
import com.github.paganini2008.devtools.RandomUtils;
import com.github.paganini2008.devtools.collection.ConcurrentSortedBoundedMap;
import com.github.paganini2008.devtools.time.InstantUtils;
import com.github.paganini2008.devtools.time.MergeableTimeSlotMap;
import com.github.paganini2008.springplayer.common.gateway.monitor.StatisticalUnit.HttpStatusCode;

/**
 * 
 * StatisticalTimeWindowMap
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class StatisticalTimeWindowMap<T extends UserMetric<T>> extends MergeableTimeSlotMap<T> {

	private static final long serialVersionUID = 6917523596516463845L;

	public StatisticalTimeWindowMap() {
		this(5, TimeWindowUnit.MINUTE, 60);
	}

	public StatisticalTimeWindowMap(int span, TimeWindowUnit timeWindowUnit, int bufferSize) {
		super(new ConcurrentSortedBoundedMap<>(Integer.min(bufferSize, timeWindowUnit.sizeOf(span, 1))), span,
				timeWindowUnit.getTimeSlot());
		this.timeWindowUnit = timeWindowUnit;
		this.bufferSize = bufferSize;
	}

	private final TimeWindowUnit timeWindowUnit;
	private final int bufferSize;

	public TimeWindowUnit getTimeWindowUnit() {
		return timeWindowUnit;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public static void main(String[] args) {
		int N = 100000;
		StatisticalTimeWindowMap<HttpStatusCode> timeWindowMap = new StatisticalTimeWindowMap<>(5, TimeWindowUnit.MINUTE, 60);
		for (int i = 0; i < N; i++) {
			Instant instant = randomInstant();
			timeWindowMap.merge(instant, new HttpStatusCode(randomHttpStatus(), instant.toEpochMilli()));
		}
		timeWindowMap.entrySet().forEach(e -> {
			System.out.println(e.getKey().atOffset(ZoneOffset.of("+8")) + "\t" + e.getValue().toEntries());
		});
	}

	private static HttpStatus randomHttpStatus() {
		return RandomUtils.randomEnum(HttpStatus.class);
	}

	private static Instant randomInstant() {
		LocalDateTime ldt = RandomDateUtils.randomLocalDateTime("2022-04-25 23:00:00", "2022-04-26 02:59:59",
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return InstantUtils.toInstant(ldt);
	}
}
