package com.github.paganini2008.springplayer.gateway.monitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.devtools.collection.LruList;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.devtools.time.DateUtils;
import com.github.paganini2008.devtools.time.LocalDateTimeUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * StatisticalUnit
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class StatisticalUnit {

	private final SummaryInfo summary;
	private final StatisticalTimeWindowMap<ResponseTime> rt;
	private final StatisticalTimeWindowMap<HttpStatusCode> httpStatus;
	private final StatisticalTimeWindowMap<Concurrency> cc;
	private final StatisticalTimeWindowMap<Qps> qps;

	private final int span;
	private final TimeWindowUnit timeWindowUnit;
	private final int bufferSize;

	private boolean asc = true;
	private final List<HttpTrace> traces = new LruList<HttpTrace>(10);

	public StatisticalUnit() {
		this(5, TimeWindowUnit.MINUTE, 60);
	}

	public StatisticalUnit(int span, TimeWindowUnit timeWindowUnit, int bufferSize) {
		this.summary = new SummaryInfo();
		this.rt = new StatisticalTimeWindowMap<>(span, timeWindowUnit, bufferSize);
		this.httpStatus = new StatisticalTimeWindowMap<>(span, timeWindowUnit, bufferSize);
		this.cc = new StatisticalTimeWindowMap<>(span, timeWindowUnit, bufferSize);
		this.qps = new StatisticalTimeWindowMap<>(span, timeWindowUnit, bufferSize);
		this.span = span;
		this.timeWindowUnit = timeWindowUnit;
		this.bufferSize = bufferSize;
	}

	public int getSpan() {
		return span;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public TimeWindowUnit getTimeWindowUnit() {
		return timeWindowUnit;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}

	public List<HttpTrace> getTraces() {
		return traces;
	}

	private void render(String metric, Map<Instant, Metric> sequence, Map<String, Map<String, Object>> renderer) {
		String time;
		for (Map.Entry<Instant, Metric> entry : sequence.entrySet()) {
			Metric current = entry.getValue();
			time = entry.getKey().atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateTimeFormatter);
			Map<String, Object> data = MapUtils.get(renderer, time, () -> {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(metric, renderNullMap(metric, current.getTimestamp()));
				return map;
			});
			data.put(metric, current.toEntries());
		}
	}

	public Map<String, Object> summary() {
		Map<String, Object> data = new HashMap<>();
		Summary<ResponseTime> rt = summary.getRt();
		data.put("rt", rt.get() != null ? rt.get().toEntries() : ResponseTime.renderNull(System.currentTimeMillis()));

		Summary<HttpStatusCode> httpStatus = summary.getHttpStatus();
		data.put("httpStatus",
				httpStatus.get() != null ? httpStatus.get().toEntries() : HttpStatusCode.renderNull(System.currentTimeMillis()));

		Summary<Concurrency> cc = summary.getCc();
		data.put("cc", cc.get() != null ? cc.get().toEntries() : Concurrency.renderNull(System.currentTimeMillis()));

		Summary<Qps> qps = summary.getQps();
		data.put("qps", qps.get() != null ? qps.get().toEntries() : Qps.renderNull(System.currentTimeMillis()));

		List<HttpTrace> httpTraces = new ArrayList<>(getTraces());
		httpTraces.sort((a, b) -> Long.valueOf(b.getTimestamp()).compareTo(Long.valueOf(a.getTimestamp())));
		data.put("traces", httpTraces);

		data.put("latestExecutionTime", LocalDateTimeUtils.toLocalDateTime(summary.getLatestTimestamp(), null));
		return data;
	}

	public Map<String, Map<String, Object>> sequence(Date startTime) {
		Instant from = getTimeWindowUnit().locate(Instant.now(), span);

		Instant value;
		Map<String, Map<String, Object>> renderer = new LinkedHashMap<String, Map<String, Object>>();

		Map<Instant, Metric> data = rt.toMap().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
		render("rt", data, renderer);
		if (rt.size() > 0) {
			value = CollectionUtils.getFirst(rt.toMap().entrySet()).getKey();
			from = from.compareTo(value) < 0 ? from : value;
		}

		data = httpStatus.toMap().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
		render("httpStatus", data, renderer);
		if (httpStatus.size() > 0) {
			value = CollectionUtils.getFirst(httpStatus.toMap().entrySet()).getKey();
			from = from.compareTo(value) < 0 ? from : value;
		}

		data = cc.toMap().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
		render("cc", data, renderer);
		if (cc.size() > 0) {
			value = CollectionUtils.getFirst(cc.toMap().entrySet()).getKey();
			from = from.compareTo(value) < 0 ? from : value;
		}

		data = qps.toMap().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
		render("qps", data, renderer);
		if (qps.size() > 0) {
			value = CollectionUtils.getFirst(qps.toMap().entrySet()).getKey();
			from = from.compareTo(value) < 0 ? from : value;
		}

		from = timeWindowUnit.locate(startTime != null ? startTime.toInstant() : from, span);
		return render(new String[] { "rt", "httpStatus", "cc", "qps" }, renderer, from);
	}

	private static Map<String, Object> renderNullMap(String type, long timestamp) {
		switch (type) {
		case "rt":
			return ResponseTime.renderNull(timestamp);
		case "httpStatus":
			return HttpStatusCode.renderNull(timestamp);
		case "cc":
			return Concurrency.renderNull(timestamp);
		case "qps":
			return Qps.renderNull(timestamp);
		default:
			throw new UnsupportedOperationException();
		}
	}

	protected final Map<String, Map<String, Object>> render(String[] metrics, Map<String, Map<String, Object>> renderer, Instant from) {
		int span = getSpan();
		int bufferSize = getBufferSize();
		boolean asc = this.asc;

		TimeWindowUnit timeWindow = getTimeWindowUnit();
		Date startTime;

		Instant now = timeWindowUnit.locate(Instant.now(), span);
		if (asc) {
			Date date = Date.from(from);
			int amount = span * bufferSize;
			Date endTime = DateUtils.addField(date, timeWindow.getCalendarField(), amount);
			if (endTime.compareTo(new Date()) <= 0) {
				startTime = Date.from(now);
			} else {
				startTime = date;
			}
		} else {
			startTime = Date.from(now);
		}
		Map<String, Map<String, Object>> sequentialMap = asc
				? timeWindow.ascendingMap(startTime, span, bufferSize, metrics, dateTimeFormatter, (metrc, timeInMs) -> {
					return renderNullMap(metrc, timeInMs);
				})
				: timeWindow.descendingMap(startTime, span, bufferSize, metrics, dateTimeFormatter, (metrc, timeInMs) -> {
					return renderNullMap(metrc, timeInMs);
				});
		String datetime;
		for (Map.Entry<String, Map<String, Object>> entry : renderer.entrySet()) {
			datetime = entry.getKey();
			if (sequentialMap.containsKey(datetime)) {
				sequentialMap.put(datetime, entry.getValue());
			}
		}
		return sequentialMap;
	}

	public static class SummaryInfo {

		private final Summary<ResponseTime> rt = new Summary<ResponseTime>();
		private final Summary<HttpStatusCode> httpStatus = new Summary<HttpStatusCode>();
		private final Summary<Concurrency> cc = new Summary<Concurrency>();
		private final Summary<Qps> qps = new Summary<Qps>();
		private volatile long latestTotalExecutionCount;

		public long getTotalExecutionCount() {
			long maxCount = Long.max(rt.get() != null ? rt.get().getCount() : 0,
					httpStatus.get() != null ? httpStatus.get().getCount() : 0);
			maxCount = Long.max(maxCount, cc.get() != null ? cc.get().getCount() : 0);
			return maxCount;
		}

		public long getLatestTimestamp() {
			long latestTimestamp = Long.max(rt.get() != null ? rt.get().getTimestamp() : 0,
					httpStatus.get() != null ? httpStatus.get().getTimestamp() : 0);
			latestTimestamp = Long.max(latestTimestamp, cc.get() != null ? cc.get().getTimestamp() : 0);
			if (latestTimestamp == 0) {
				latestTimestamp = System.currentTimeMillis();
			}
			return latestTimestamp;
		}

		public long getLatestTotalExecutionCount() {
			return latestTotalExecutionCount;
		}

		public void setLatestTotalExecutionCount(long latestTotalExecutionCount) {
			this.latestTotalExecutionCount = latestTotalExecutionCount;
		}

		public Summary<ResponseTime> getRt() {
			return rt;
		}

		public Summary<HttpStatusCode> getHttpStatus() {
			return httpStatus;
		}

		public Summary<Concurrency> getCc() {
			return cc;
		}

		public Summary<Qps> getQps() {
			return qps;
		}

	}

	public SummaryInfo getSummary() {
		return summary;
	}

	public StatisticalTimeWindowMap<ResponseTime> getRt() {
		return rt;
	}

	public StatisticalTimeWindowMap<HttpStatusCode> getHttpStatus() {
		return httpStatus;
	}

	public StatisticalTimeWindowMap<Concurrency> getCc() {
		return cc;
	}

	public StatisticalTimeWindowMap<Qps> getQps() {
		return qps;
	}

	@Getter
	@Setter
	public static class ResponseTime implements UserMetric<ResponseTime>, Comparable<ResponseTime> {

		private long totalValue;
		private long highestValue;
		private long lowestValue;
		private int count;
		private long timestamp;
		private long baseline;

		public ResponseTime() {
			this.timestamp = System.currentTimeMillis();
		}

		public ResponseTime(long timestamp) {
			long elapsed = System.currentTimeMillis() - timestamp;
			this.totalValue = elapsed;
			this.highestValue = elapsed;
			this.lowestValue = elapsed;
			this.count = 1;
			this.timestamp = timestamp;
		}

		public long getMiddleValue() {
			if (count > 2) {
				return (totalValue - highestValue - lowestValue) / (count - 2);
			} else if (count == 1 || count == 2) {
				return totalValue / count;
			} else {
				return 0;
			}
		}

		@Override
		public ResponseTime empty() {
			return new ResponseTime();
		}

		@Override
		public ResponseTime merge(ResponseTime update) {
			ResponseTime rt = new ResponseTime();
			rt.setTotalValue(getTotalValue() + update.getTotalValue());
			rt.setHighestValue(Long.max(getHighestValue(), update.getHighestValue()));
			rt.setLowestValue(getLowestValue() > 0 ? Long.min(getLowestValue(), update.getLowestValue()) : update.getLowestValue());
			rt.setCount(getCount() + update.getCount());
			rt.setTimestamp(update.getTimestamp());
			rt.setBaseline(update.getBaseline());
			return rt;
		}

		@Override
		public Map<String, Object> toEntries() {
			Map<String, Object> map = new HashMap<>();
			map.put("highestValue", getHighestValue());
			map.put("lowestValue", getLowestValue());
			map.put("middleValue", getMiddleValue());
			map.put("count", getCount());
			map.put("growthRate", getGrowthRate());
			map.put("timestamp", getTimestamp());
			return map;
		}

		@Override
		public Number getValue() {
			return getMiddleValue();
		}

		@Override
		public int compareTo(ResponseTime other) {
			return Long.valueOf(other.getMiddleValue()).compareTo(Long.valueOf(getMiddleValue()));
		}

		public static Map<String, Object> renderNull(long ms) {
			Map<String, Object> map = new HashMap<>();
			map.put("highestValue", 0);
			map.put("lowestValue", 0);
			map.put("middleValue", 0);
			map.put("count", 0);
			map.put("growthRate", 0);
			map.put("timestamp", ms);
			return map;
		}

		public BigDecimal getGrowthRate() {
			if (baseline <= 0) {
				return BigDecimal.ZERO;
			}
			long growth = getValue().longValue() - baseline;
			if (growth < 0) {
				return BigDecimal.ZERO;
			}
			return BigDecimal.valueOf(growth).divide(BigDecimal.valueOf(baseline), 3, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
		}

		public void setBaseline(long baseline) {
			if (baseline > 0) {
				this.baseline = baseline;
			}
		}

	}

	@Getter
	@Setter
	public static class HttpStatusCode implements UserMetric<HttpStatusCode>, Comparable<HttpStatusCode> {

		private int countOf1xx;
		private int countOf2xx;
		private int countOf3xx;
		private int countOf4xx;
		private int countOf5xx;
		private int count;
		private long timestamp;
		private int baseline;

		public HttpStatusCode() {
		}

		public HttpStatusCode(HttpStatus httpStatus, long timestamp) {
			count = 1;
			if (httpStatus.is1xxInformational()) {
				countOf1xx = 1;
			} else if (httpStatus.is2xxSuccessful()) {
				countOf2xx = 1;
			} else if (httpStatus.is3xxRedirection()) {
				countOf3xx = 1;
			} else if (httpStatus.is4xxClientError()) {
				countOf4xx = 1;
			} else if (httpStatus.is5xxServerError()) {
				countOf5xx = 1;
			}

			this.timestamp = timestamp;
		}

		public int getFailureCount() {
			return getCount() - getSuccessCount();
		}

		public int getSuccessCount() {
			return getCountOf2xx();
		}

		@Override
		public Number getValue() {
			return getFailureCount();
		}

		@Override
		public HttpStatusCode empty() {
			return new HttpStatusCode();
		}

		@Override
		public HttpStatusCode merge(HttpStatusCode update) {
			HttpStatusCode counter = new HttpStatusCode();
			counter.setCountOf1xx(getCountOf1xx() + update.getCountOf1xx());
			counter.setCountOf2xx(getCountOf2xx() + update.getCountOf2xx());
			counter.setCountOf3xx(getCountOf3xx() + update.getCountOf3xx());
			counter.setCountOf4xx(getCountOf4xx() + update.getCountOf4xx());
			counter.setCountOf5xx(getCountOf5xx() + update.getCountOf5xx());
			counter.setCount(getCount() + update.getCount());
			counter.setTimestamp(update.getTimestamp());
			counter.setBaseline(update.getBaseline());
			return counter;
		}

		public BigDecimal getSuccessRate() {
			if (count == 0) {
				return BigDecimal.ZERO;
			}
			return BigDecimal.valueOf((float) countOf2xx / count).multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP);
		}

		@Override
		public Map<String, Object> toEntries() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("countOf1xx", getCountOf1xx());
			map.put("countOf2xx", getCountOf2xx());
			map.put("countOf3xx", getCountOf3xx());
			map.put("countOf4xx", getCountOf4xx());
			map.put("countOf5xx", getCountOf5xx());
			map.put("count", getCount());
			map.put("successRate", getSuccessRate());
			map.put("growthRate", getGrowthRate());
			map.put("timestamp", getTimestamp());
			return map;
		}

		@Override
		public int compareTo(HttpStatusCode other) {
			return Integer.valueOf(other.getFailureCount()).compareTo(Integer.valueOf(getFailureCount()));
		}

		public static Map<String, Object> renderNull(long timestamp) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("countOf1xx", 0);
			map.put("countOf2xx", 0);
			map.put("countOf3xx", 0);
			map.put("countOf4xx", 0);
			map.put("countOf5xx", 0);
			map.put("count", 0);
			map.put("successRate", 0);
			map.put("growthRate", 0);
			map.put("timestamp", timestamp);
			return map;
		}

		public BigDecimal getGrowthRate() {
			if (baseline <= 0) {
				return BigDecimal.ZERO;
			}
			int growth = getValue().intValue() - baseline;
			if (growth <= 0) {
				return BigDecimal.ZERO;
			}
			return BigDecimal.valueOf(growth).divide(BigDecimal.valueOf(baseline), 3, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
		}

		public void setBaseline(int baseline) {
			if (baseline > 0) {
				this.baseline = baseline;
			}
		}

	}

	@Getter
	@Setter
	public static class Qps implements UserMetric<Qps>, Comparable<Qps> {

		private int totalValue;
		private int highestValue;
		private int lowestValue;
		private int count;
		private long timestamp;
		private int baseline;

		public Qps() {
			this.timestamp = System.currentTimeMillis();
		}

		public Qps(int value, long timestamp) {
			this.totalValue = value;
			this.highestValue = value;
			this.lowestValue = value;
			this.count = 1;
			this.timestamp = timestamp;
		}

		@Override
		public Qps empty() {
			return new Qps();
		}

		@Override
		public Qps merge(Qps update) {
			Qps q = new Qps();
			q.setTotalValue(getTotalValue() + update.getTotalValue());
			q.setHighestValue(Integer.max(getHighestValue(), update.getHighestValue()));
			q.setLowestValue(getLowestValue() > 0 ? Integer.min(getLowestValue(), update.getLowestValue()) : update.getLowestValue());
			q.setCount(getCount() + update.getCount());
			q.setTimestamp(update.getTimestamp());
			q.setBaseline(update.getBaseline());
			return q;
		}

		public int getMiddleValue() {
			if (count > 2) {
				return (totalValue - highestValue - lowestValue) / (count - 2);
			} else if (count == 1 || count == 2) {
				return totalValue / count;
			} else {
				return 0;
			}
		}

		@Override
		public int compareTo(Qps other) {
			return Long.valueOf(other.getMiddleValue()).compareTo(Long.valueOf(getMiddleValue()));
		}

		@Override
		public Number getValue() {
			return getMiddleValue();
		}

		@Override
		public Map<String, Object> toEntries() {
			Map<String, Object> map = new HashMap<>();
			map.put("highestValue", getHighestValue());
			map.put("lowestValue", getLowestValue());
			map.put("middleValue", getMiddleValue());
			map.put("count", getCount());
			map.put("growthRate", getGrowthRate());
			map.put("timestamp", getTimestamp());
			return map;
		}

		public static Map<String, Object> renderNull(long ms) {
			Map<String, Object> map = new HashMap<>();
			map.put("highestValue", 0);
			map.put("lowestValue", 0);
			map.put("middleValue", 0);
			map.put("count", 0);
			map.put("growthRate", 0);
			map.put("timestamp", ms);
			return map;
		}

		public BigDecimal getGrowthRate() {
			if (baseline <= 0) {
				return BigDecimal.ZERO;
			}
			int growth = getValue().intValue() - baseline;
			if (growth <= 0) {
				return BigDecimal.ZERO;
			}
			return BigDecimal.valueOf(growth).divide(BigDecimal.valueOf(baseline), 3, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
		}

		public void setBaseline(int baseline) {
			if (baseline > 0) {
				this.baseline = baseline;
			}
		}

	}

	@Getter
	@Setter
	public static class Concurrency implements UserMetric<Concurrency>, Comparable<Concurrency> {

		private int totalValue;
		private int highestValue;
		private int lowestValue;
		private int count;
		private long timestamp;
		private int baseline;

		public Concurrency() {
			this.timestamp = System.currentTimeMillis();
		}

		public Concurrency(int value, long timestamp) {
			this.totalValue = value;
			this.highestValue = value;
			this.lowestValue = value;
			this.count = 1;
			this.timestamp = timestamp;
		}

		@Override
		public Concurrency empty() {
			return new Concurrency();
		}

		@Override
		public Concurrency merge(Concurrency update) {
			Concurrency concurrency = new Concurrency();
			concurrency.setTotalValue(getTotalValue() + update.getTotalValue());
			concurrency.setHighestValue(Integer.max(getHighestValue(), update.getHighestValue()));
			concurrency.setLowestValue(
					getLowestValue() > 0 ? Integer.min(getLowestValue(), update.getLowestValue()) : update.getLowestValue());
			concurrency.setCount(getCount() + update.getCount());
			concurrency.setTimestamp(update.getTimestamp());
			concurrency.setBaseline(update.getBaseline());
			return concurrency;
		}

		public int getMiddleValue() {
			if (count > 2) {
				return (totalValue - highestValue - lowestValue) / (count - 2);
			} else if (count == 1 || count == 2) {
				return totalValue / count;
			} else {
				return 0;
			}
		}

		@Override
		public int compareTo(Concurrency other) {
			return Long.valueOf(other.getMiddleValue()).compareTo(Long.valueOf(getMiddleValue()));
		}

		@Override
		public Map<String, Object> toEntries() {
			Map<String, Object> map = new HashMap<>();
			map.put("highestValue", getHighestValue());
			map.put("lowestValue", getLowestValue());
			map.put("middleValue", getMiddleValue());
			map.put("count", getCount());
			map.put("growthRate", getGrowthRate());
			map.put("timestamp", timestamp);
			return map;
		}

		public static Map<String, Object> renderNull(long ms) {
			Map<String, Object> map = new HashMap<>();
			map.put("highestValue", 0);
			map.put("lowestValue", 0);
			map.put("middleValue", 0);
			map.put("count", 0);
			map.put("growthRate", 0);
			map.put("timestamp", ms);
			return map;
		}

		@Override
		public Number getValue() {
			return getMiddleValue();
		}

		public BigDecimal getGrowthRate() {
			if (baseline <= 0) {
				return BigDecimal.ZERO;
			}
			int growth = getValue().intValue() - baseline;
			if (growth <= 0) {
				return BigDecimal.ZERO;
			}
			return BigDecimal.valueOf(growth).divide(BigDecimal.valueOf(baseline), 3, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
		}

		public void setBaseline(int baseline) {
			if (baseline > 0) {
				this.baseline = baseline;
			}
		}

	}

}
