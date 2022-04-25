package com.github.paganini2008.springplayer.gateway.monitor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.LruMap;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.devtools.multithreads.Executable;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.springplayer.gateway.monitor.StatisticalUnit.Concurrency;
import com.github.paganini2008.springplayer.gateway.monitor.StatisticalUnit.HttpStatusCode;
import com.github.paganini2008.springplayer.gateway.monitor.StatisticalUnit.Qps;
import com.github.paganini2008.springplayer.gateway.monitor.StatisticalUnit.ResponseTime;
import com.github.paganini2008.springplayer.gateway.route.RedisCachedRouteDefinitionWriter;
import com.github.paganini2008.springplayer.gateway.route.RoutePublishEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ApiWatcherContext
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class ApiWatcherContext {

	private final PathMatcherMap<StatisticalUnit> statisticsUnits = new PathMatcherMap<>();
	private final PathMatcherMap<DiagnosticUnit> diagnosticUnits = new PathMatcherMap<>();
	private final PathMatcherMap<AtomicInteger> concurrencyMap = new PathMatcherMap<AtomicInteger>();

	private final EnhancedPathMatcherMap<StatisticalUnit> labelStatisticsMap = new EnhancedPathMatcherMap<StatisticalUnit>();
	private final EnhancedPathMatcherMap<AtomicInteger> labelConcurrencyMap = new EnhancedPathMatcherMap<AtomicInteger>();
	private final PathMatcherMap<Label[]> pathPatternLabels = new PathMatcherMap<Label[]>();

	private LabelExtractor labelExtractor = new SimpleLabelExtractor();

	public ApiWatcherContext() {
		watchPath("/**");
	}

	public void watchPath(String pathPattern, Label... labels) {
		statisticsUnits.putIfAbsent(pathPattern, new StatisticalUnit());
		diagnosticUnits.putIfAbsent(pathPattern, new DiagnosticUnit());
		concurrencyMap.putIfAbsent(pathPattern, new AtomicInteger(0));

		if (ArrayUtils.isNotEmpty(labels)) {
			String label = ArrayUtils.join(labels, "+");
			labelStatisticsMap.putIfAbsent(pathPattern + "+" + label, new StatisticalUnit());
			labelConcurrencyMap.putIfAbsent(pathPattern + "+" + label, new AtomicInteger(0));
			pathPatternLabels.put(pathPattern, labels);
		}
		log.info("Watch Path: " + pathPattern);
	}

	public List<HttpTrace> getLatestHttpTraces(String pathPattern) {
		StatisticalUnit statisticalUnit = statisticsUnits.get(pathPattern);
		if (statisticalUnit == null) {
			return Collections.emptyList();
		}
		List<HttpTrace> httpTraces = new ArrayList<>(statisticalUnit.getTraces());
		httpTraces.sort((a, b) -> Long.valueOf(b.getTimestamp()).compareTo(Long.valueOf(a.getTimestamp())));
		return httpTraces;
	}

	public void unwatchPath(String pathPattern) {
		statisticsUnits.remove(pathPattern);
		diagnosticUnits.remove(pathPattern);
		concurrencyMap.remove(pathPattern);
		pathPatternLabels.remove(pathPattern);
		log.info("Unwatch Path: " + pathPattern);
	}

	public int getConcurrents(HttpTrace httpTrace) {
		AtomicInteger counter = null;
		String path = httpTrace.getPath();
		if (pathPatternLabels.containsKey(path)) {
			Label[] labels = pathPatternLabels.get(path);
			if (matchesLabels(labels, httpTrace)) {
				String label = ArrayUtils.join(labels, "+");
				String labelPath = path + "+" + label;
				counter = labelConcurrencyMap.get(labelPath);
			}
		} else {
			counter = concurrencyMap.get(path);
		}
		return counter != null ? counter.get() : 0;
	}

	public void incrementConcurrents(HttpTrace httpTrace) {
		AtomicInteger counter = null;
		String path = httpTrace.getPath();
		if (pathPatternLabels.containsKey(path)) {
			Label[] labels = pathPatternLabels.get(path);
			if (matchesLabels(labels, httpTrace)) {
				String label = ArrayUtils.join(labels, "+");
				String labelPath = path + "+" + label;
				counter = labelConcurrencyMap.get(labelPath);
			}
		} else {
			counter = concurrencyMap.get(path);
		}
		if (counter != null) {
			counter.incrementAndGet();
		}
	}

	public void decrementConcurrents(HttpTrace httpTrace) {
		AtomicInteger counter = null;
		String path = httpTrace.getPath();
		if (pathPatternLabels.containsKey(path)) {
			Label[] labels = pathPatternLabels.get(path);
			if (matchesLabels(labels, httpTrace)) {
				String label = ArrayUtils.join(labels, "+");
				String labelPath = path + "+" + label;
				counter = labelConcurrencyMap.get(labelPath);
			}
		} else {
			counter = concurrencyMap.get(path);
		}
		if (counter != null) {
			counter.decrementAndGet();
		}
	}

	public void setLabelExtractor(LabelExtractor labelExtractor) {
		this.labelExtractor = labelExtractor;
	}

	public Map<String, Object> getLatestQps() {
		return getLatest(StatisticalUnit::getQps);
	}

	public Map<String, Object> getLatestCc() {
		return getLatest(StatisticalUnit::getCc);
	}

	public Map<String, Object> getLatestHttpStatus() {
		return getLatest(StatisticalUnit::getHttpStatus);
	}

	public Map<String, Object> getLatestRt() {
		return getLatest(StatisticalUnit::getRt);
	}

	private <T extends UserMetric<T>> Map<String, Object> getLatest(Function<StatisticalUnit, StatisticalTimeWindowMap<T>> fun) {
		final int N = 5;
		Map<String, T> data = statisticsUnits.entrySet().stream().filter(e -> testLatest(e.getValue(), fun)).collect(Collectors.toMap(e -> {
			return e.getKey();
		}, e -> {
			Map.Entry<Instant, T> entry = MapUtils.getLastEntry(fun.apply(e.getValue()));
			return entry.getValue();
		}));
		Map<String, Object> resultMap = data.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
		if (resultMap.size() > N) {
			List<Map.Entry<String, Object>> list = new ArrayList<>(resultMap.entrySet());
			resultMap.clear();
			MapUtils.putAll(resultMap, list.subList(0, Math.min(N, list.size())));
		}
		return resultMap;
	}

	private <T extends UserMetric<T>> boolean testLatest(StatisticalUnit st, Function<StatisticalUnit, StatisticalTimeWindowMap<T>> fun) {
		StatisticalTimeWindowMap<T> timeWindowMap = fun.apply(st);
		if (timeWindowMap.isEmpty()) {
			return false;
		}
		Instant compared = st.getTimeWindowUnit().locate(Instant.now(), st.getSpan());
		return timeWindowMap.containsKey(compared);
	}

	public Collection<String> paths() {
		return Collections.unmodifiableCollection(new TreeSet<>(statisticsUnits.keySet()));
	}

	public Map<String, Object> summary(String pathPattern) {
		StatisticalUnit statisticalUnit = statisticsUnits.get(pathPattern);
		if (statisticalUnit == null) {
			return Collections.emptyMap();
		}
		return statisticalUnit.summary();
	}

	public Map<String, Map<String, Object>> sequence(String pathPattern, Date startTime) {
		StatisticalUnit statisticalUnit = statisticsUnits.get(pathPattern);
		if (statisticalUnit == null) {
			return Collections.emptyMap();
		}
		return statisticalUnit.sequence(startTime);
	}

	public Map<String, Map<String, List<HttpTrace>>> searchTraces(String pathPattern, Date startDate, Date endDate) {
		DiagnosticUnit diagnosticUnit = diagnosticUnits.get(pathPattern);
		if (diagnosticUnit == null) {
			return Collections.emptyMap();
		}
		if (startDate != null) {
			return diagnosticUnit.search(startDate, endDate);
		}
		return diagnosticUnit.searchLatest();
	}

	private final LruMap<Instant, Instant> lastInstants = new LruMap<>();

	private Instant getLastInstant(Instant instant, StatisticalUnit statisticalUnit) {
		Instant newInstant = statisticalUnit.getTimeWindowUnit().locate(instant, statisticalUnit.getSpan());
		return MapUtils.get(lastInstants, newInstant,
				() -> getLastInstant(instant, statisticalUnit.getSpan(), statisticalUnit.getTimeWindowUnit().getChronoUnit()));
	}

	private Instant getLastInstant(Instant time, int span, ChronoUnit chronoUnit) {
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime ldt = time.atZone(zoneId).toLocalDateTime();
		ldt = ldt.plus(-1 * span, chronoUnit);
		return ldt.atZone(zoneId).toInstant();
	}

	public void record(HttpResponseTrace response) {
		HttpRequestTrace requestTrace = response.getRequest();
		final long timestamp = requestTrace.getTimestamp();
		final String path = requestTrace.getPath();

		Instant instant = Instant.ofEpochMilli(timestamp);
		StatisticalUnit statisticalUnit = statisticsUnits.get(path);
		Instant lastInstant = getLastInstant(instant, statisticalUnit);

		statisticalUnit.getSummary().getRt().merge(new StatisticalUnit.ResponseTime(timestamp));
		statisticalUnit.getSummary().getHttpStatus().merge(new StatisticalUnit.HttpStatusCode(response.getStatus(), timestamp));
		int cons = concurrencyMap.get(path).get();
		statisticalUnit.getSummary().getCc().merge(new StatisticalUnit.Concurrency(cons, timestamp));

		statisticalUnit.getRt().merge(instant, newResponseTimeMetric(timestamp, lastInstant, statisticalUnit));
		statisticalUnit.getHttpStatus().merge(instant,
				newHttpStatusCodeMetric(response.getStatus(), timestamp, lastInstant, statisticalUnit));
		statisticalUnit.getCc().merge(instant, newConcurrencyMetric(cons, timestamp, lastInstant, statisticalUnit));

		statisticalUnit.getTraces().add(response);

		DiagnosticUnit diagnosticUnit = diagnosticUnits.get(path);
		diagnosticUnit.check(response);

		if (pathPatternLabels.containsKey(path)) {
			Label[] labels = pathPatternLabels.get(path);
			if (matchesLabels(labels, requestTrace)) {
				String label = ArrayUtils.join(labels, "+");
				String labelPath = path + "+" + label;
				statisticalUnit = labelStatisticsMap.get(labelPath);

				statisticalUnit.getSummary().getRt().merge(new StatisticalUnit.ResponseTime(timestamp));
				statisticalUnit.getSummary().getHttpStatus().merge(new StatisticalUnit.HttpStatusCode(response.getStatus(), timestamp));
				cons = labelConcurrencyMap.get(labelPath).get();
				statisticalUnit.getSummary().getCc().merge(new StatisticalUnit.Concurrency(cons, timestamp));

				statisticalUnit.getRt().merge(instant, newResponseTimeMetric(timestamp, lastInstant, statisticalUnit));
				statisticalUnit.getHttpStatus().merge(instant,
						newHttpStatusCodeMetric(response.getStatus(), timestamp, lastInstant, statisticalUnit));
				statisticalUnit.getCc().merge(instant, newConcurrencyMetric(cons, timestamp, lastInstant, statisticalUnit));

				statisticalUnit.getTraces().add(response);
			}
		}
	}

	private StatisticalUnit.ResponseTime newResponseTimeMetric(long timestamp, Instant lastInstant, StatisticalUnit statisticalUnit) {
		StatisticalUnit.ResponseTime rt = new ResponseTime(timestamp);
		if (lastInstant != null && statisticalUnit.getRt().containsKey(lastInstant)) {
			Metric metric = statisticalUnit.getRt().get(lastInstant);
			long baseline = metric.getValue().longValue();
			rt.setBaseline(baseline);
		}
		return rt;
	}

	private StatisticalUnit.HttpStatusCode newHttpStatusCodeMetric(HttpStatus responseStatus, long timestamp, Instant lastInstant,
			StatisticalUnit statisticalUnit) {
		StatisticalUnit.HttpStatusCode httpStatus = new HttpStatusCode(responseStatus, timestamp);
		if (lastInstant != null && statisticalUnit.getHttpStatus().containsKey(lastInstant)) {
			Metric metric = statisticalUnit.getHttpStatus().get(lastInstant);
			int baseline = metric.getValue().intValue();
			httpStatus.setBaseline(baseline);
		}
		return httpStatus;
	}

	private StatisticalUnit.Concurrency newConcurrencyMetric(int cons, long timestamp, Instant lastInstant,
			StatisticalUnit statisticalUnit) {
		StatisticalUnit.Concurrency cc = new Concurrency(cons, timestamp);
		if (lastInstant != null && statisticalUnit.getCc().containsKey(lastInstant)) {
			Metric metric = statisticalUnit.getCc().get(lastInstant);
			int baseline = metric.getValue().intValue();
			cc.setBaseline(baseline);
		}
		return cc;
	}

	private StatisticalUnit.Qps newQpsMetric(int qpsValue, long timestamp, Instant lastInstant, StatisticalUnit statisticalUnit) {
		StatisticalUnit.Qps qps = new Qps(qpsValue, timestamp);
		if (lastInstant != null && statisticalUnit.getQps().containsKey(lastInstant)) {
			Metric metric = statisticalUnit.getQps().get(lastInstant);
			int baseline = metric.getValue().intValue();
			qps.setBaseline(baseline);
		}
		return qps;
	}

	private boolean matchesLabels(Label[] labels, HttpTrace httpTrace) {
		return Arrays.stream(labels).allMatch(label -> {
			Object headerValue = labelExtractor.extractLabel(label.getName(), httpTrace);
			return label.getValue().equals(Objects.toString(headerValue, ""));
		});

	}

	private class QpsCalcTask implements Executable {

		private Timer timer;

		public void start() {
			this.timer = ThreadUtils.scheduleWithFixedDelay(this, 1, TimeUnit.SECONDS);
		}

		public void stop() {
			if (timer != null) {
				timer.cancel();
			}
		}

		@Override
		public boolean execute() throws Throwable {
			if (statisticsUnits.size() > 0) {
				process(statisticsUnits);
			}
			if (labelStatisticsMap.size() > 0) {
				process(labelStatisticsMap);
			}
			return true;
		}

		private void process(Map<String, StatisticalUnit> map) {
			map.entrySet().stream().forEach(e -> {
				long now = System.currentTimeMillis();
				StatisticalUnit statisticalUnit = e.getValue();
				long currentCount = statisticalUnit.getSummary().getTotalExecutionCount();
				if (currentCount > 0) {
					int qps = (int) (currentCount - statisticalUnit.getSummary().getLatestTotalExecutionCount());
					statisticalUnit.getSummary().setLatestTotalExecutionCount(currentCount);

					statisticalUnit.getSummary().getQps().merge(new StatisticalUnit.Qps(qps, now));

					Instant instant = Instant.ofEpochMilli(statisticalUnit.getSummary().getLatestTimestamp());
					Instant lastInstant = getLastInstant(instant, statisticalUnit);
					statisticalUnit.getQps().merge(instant, newQpsMetric(qps, now, lastInstant, statisticalUnit));
				}
			});
		}
	}

	@Autowired
	private RedisCachedRouteDefinitionWriter routeDefinitionWriter;

	@Value("${spring.cloud.gateway.monitor.path.autoAllocated:true}")
	private boolean autoAllocateEnabled;

	private QpsCalcTask qpsTask;

	@PostConstruct
	public void configure() {
		qpsTask = new QpsCalcTask();
		qpsTask.start();
	}

	@PreDestroy
	public void unconfigure() {
		if (qpsTask != null) {
			qpsTask.stop();
			qpsTask = null;
		}
	}

	@EventListener({ WebServerInitializedEvent.class, RoutePublishEvent.class })
	public void handleEvent() throws Exception {
		if (autoAllocateEnabled) {
			routeDefinitionWriter.getRouteDefinitions().subscribe(rd -> {
				rd.getPredicates().forEach(p -> {
					if ("Path".equals(p.getName()) && MapUtils.isNotEmpty(p.getArgs())) {
						String pathPattern = MapUtils.getFirstEntry(p.getArgs()).getValue();
						if (StringUtils.isNotBlank(pathPattern)) {
							watchPath(pathPattern);
						}
					}
				});
			});
		}
	}

}
