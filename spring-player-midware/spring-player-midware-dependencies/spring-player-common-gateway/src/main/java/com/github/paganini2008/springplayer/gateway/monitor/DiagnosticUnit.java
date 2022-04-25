package com.github.paganini2008.springplayer.gateway.monitor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.devtools.time.DateUtils;

/**
 * 
 * DiagnosticUnit
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class DiagnosticUnit {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final AppendableTimeWindowMap<HttpTrace> status;
	private final AppendableTimeWindowMap<HttpTrace> rt;
	private final AppendableTimeWindowMap<HttpTrace> cc;

	public DiagnosticUnit() {
		this(5, TimeWindowUnit.MINUTE, 60, 10);
	}

	public DiagnosticUnit(int span, TimeWindowUnit timeWindowUnit, int bufferSize, int maxListSize) {
		this.status = new AppendableTimeWindowMap<>(span, timeWindowUnit, bufferSize, maxListSize);
		this.rt = new AppendableTimeWindowMap<>(span, timeWindowUnit, bufferSize, maxListSize);
		this.cc = new AppendableTimeWindowMap<>(span, timeWindowUnit, bufferSize, maxListSize);
		this.span = span;
		this.timeWindowUnit = timeWindowUnit;
	}

	private final int span;
	private final TimeWindowUnit timeWindowUnit;

	private int maxRequestTimeout = HttpTrace.MAX_REQUEST_TIMEOUT;
	private int maxConcurrency = HttpTrace.MAX_CONCURRENCY;

	public void setMaxRequestTimeout(int maxRequestTimeout) {
		this.maxRequestTimeout = maxRequestTimeout;
	}

	public void setMaxConcurrency(int maxConcurrency) {
		this.maxConcurrency = maxConcurrency;
	}

	public int getSpan() {
		return span;
	}

	public TimeWindowUnit getTimeWindowUnit() {
		return timeWindowUnit;
	}

	public void check(HttpResponseTrace trace) {
		Instant time = Instant.ofEpochMilli(trace.getRequest().getTimestamp());
		if (!trace.isOk()) {
			status.append(time, trace);
		}
		if (trace.getElapsed() > maxRequestTimeout) {
			rt.append(time, trace);
		}
		if (trace.getConcurrents() > maxConcurrency) {
			cc.append(time, trace);
		}

	}

	public Map<String, Map<String, List<HttpTrace>>> search(Date startDate, Date endDate) {
		if (endDate == null) {
			endDate = new Date();
		}
		Map<String, Map<String, List<HttpTrace>>> resultMap = new LinkedHashMap<>();
		Iterator<Calendar> it = DateUtils.toIterator(startDate, endDate, span, timeWindowUnit.getCalendarField());
		while (it.hasNext()) {
			getTraces(it.next().toInstant(), resultMap);
		}
		return resultMap;
	}

	public Map<String, Map<String, List<HttpTrace>>> searchLatest() {
		Map<String, Map<String, List<HttpTrace>>> resultMap = new LinkedHashMap<>();
		getTraces(Instant.now(), resultMap);
		return resultMap;
	}

	private void getTraces(Instant time, Map<String, Map<String, List<HttpTrace>>> resultMap) {
		time = timeWindowUnit.locate(time, span);
		String dateFormat = time.atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateTimeFormatter);

		Map<String, List<HttpTrace>> data = null;
		List<HttpTrace> traces = status.get(time);
		if (traces != null) {
			data = MapUtils.get(resultMap, "status", () -> new LinkedHashMap<>());
			data.put(dateFormat, new ArrayList<>(traces));
		}

		traces = rt.get(time);
		if (traces != null) {
			data = MapUtils.get(resultMap, "rt", () -> new LinkedHashMap<>());
			data.put(dateFormat, new ArrayList<>(traces));
		}

		traces = cc.get(time);
		if (traces != null) {
			data = MapUtils.get(resultMap, "cc", () -> new LinkedHashMap<>());
			data.put(dateFormat, new ArrayList<>(traces));
		}
	}

}
