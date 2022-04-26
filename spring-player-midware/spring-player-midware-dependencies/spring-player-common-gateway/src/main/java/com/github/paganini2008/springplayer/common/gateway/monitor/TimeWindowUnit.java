package com.github.paganini2008.springplayer.common.gateway.monitor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.github.paganini2008.devtools.time.InstantUtils;
import com.github.paganini2008.devtools.time.TimeSlot;

/**
 * 
 * TimeWindowUnit
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum TimeWindowUnit {

	DAY(Calendar.DAY_OF_MONTH, ChronoUnit.DAYS, TimeSlot.DAY) {

		@Override
		public Map<String, Map<String, Object>> descendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ldt = ldt.withHour(0).withMinute(0).withSecond(0);
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);

				ldt = ldt.plusDays(-1 * span);
			}
			return data;
		}

		@Override
		public Map<String, Map<String, Object>> ascendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ldt = ldt.withHour(0).withMinute(0).withSecond(0);
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);
				ldt = ldt.plusDays(span);
			}
			return data;
		}

	},

	HOUR(Calendar.HOUR_OF_DAY, ChronoUnit.HOURS, TimeSlot.HOUR) {

		@Override
		public Map<String, Map<String, Object>> descendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ldt = ldt.withMinute(0).withSecond(0);
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);

				ldt = ldt.plusHours(-1 * span);
			}
			return data;
		}

		@Override
		public Map<String, Map<String, Object>> ascendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ldt = ldt.withMinute(0).withSecond(0);
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);
				ldt = ldt.plusHours(span);
			}
			return data;
		}

	},

	MINUTE(Calendar.MINUTE, ChronoUnit.MINUTES, TimeSlot.MINUTE) {

		@Override
		public Map<String, Map<String, Object>> descendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ldt = ldt.withSecond(0);
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);
				ldt = ldt.plusMinutes(-1 * span);
			}
			return data;
		}

		@Override
		public Map<String, Map<String, Object>> ascendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ldt = ldt.withSecond(0);
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);
				ldt = ldt.plusMinutes(span);
			}
			return data;
		}
	},

	SECOND(Calendar.SECOND, ChronoUnit.SECONDS, TimeSlot.SECOND) {

		@Override
		public Map<String, Map<String, Object>> descendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);
				ldt = ldt.plusSeconds(-1 * span);
			}
			return data;
		}

		@Override
		public Map<String, Map<String, Object>> ascendingMap(Date startTime, int span, int bufferSize, String[] metrics,
				DateTimeFormatter df, BiFunction<String, Long, Object> supplier) {
			Map<String, Map<String, Object>> data = new LinkedHashMap<String, Map<String, Object>>();
			LocalDateTime ldt = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();
			for (int i = 0; i < bufferSize; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String metric : metrics) {
					map.put(metric, supplier.apply(metric, ldt.toInstant(defaultOffset).toEpochMilli()));
				}
				data.put(ldt.format(df), map);
				ldt = ldt.plusSeconds(span);
			}
			return data;
		}

	};

	private final int calendarField;
	private final ChronoUnit chronoUnit;
	private final TimeSlot timeSlot;

	private TimeWindowUnit(int calendarField, ChronoUnit chronoUnit, TimeSlot timeSlot) {
		this.calendarField = calendarField;
		this.chronoUnit = chronoUnit;
		this.timeSlot = timeSlot;
	}

	public int getCalendarField() {
		return calendarField;
	}

	public ChronoUnit getChronoUnit() {
		return chronoUnit;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public int sizeOf(int span, int days) {
		return getTimeSlot().sizeOf(span, days);
	}

	public Instant locate(Instant time, int span) {
		return InstantUtils.toInstant(getTimeSlot().locate(time, span));
	}

	public abstract Map<String, Map<String, Object>> descendingMap(Date startTime, int span, int bufferSize, String[] metrics,
			DateTimeFormatter df, BiFunction<String, Long, Object> supplier);

	public abstract Map<String, Map<String, Object>> ascendingMap(Date startTime, int span, int bufferSize, String[] metrics,
			DateTimeFormatter df, BiFunction<String, Long, Object> supplier);

}
