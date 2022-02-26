package com.github.paganini2008.springplayer.crumb.service;

import static com.github.paganini2008.springplayer.crumb.CrumbConstants.INDEX_NAME_TRACE_SPAN;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.devtools.beans.PropertyUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.devtools.jdbc.PageRequest;
import com.github.paganini2008.devtools.jdbc.PageResponse;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;
import com.github.paganini2008.devtools.multithreads.Executable;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.PageResult;
import com.github.paganini2008.springplayer.crumb.entity.TraceSpan;
import com.github.paganini2008.springplayer.crumb.vo.TraceSpanVO;
import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.logging.common.AppLogEntry;
import com.github.paganini2008.springplayer.monitor.crumb.Span;
import com.github.paganini2008.springplayer.monitor.crumb.SpanTree;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TraceSpanService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
public class TraceSpanService implements Executable, InitializingBean, DisposableBean {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	private IdGenerator idGenerator;

	private Timer timer;
	private final List<TraceSpan> logQueue = new CopyOnWriteArrayList<TraceSpan>();

	public void saveTraceSpan(AppLogEntry entry) {
		SpanTree spanTree = JacksonUtils.parseJson(entry.getMessage(), SpanTree.class);
		doSaveTraceSpan(spanTree);
	}

	private void doSaveTraceSpan(SpanTree spanTree) {
		logQueue.add(convertAsTraceSpan(spanTree.getSpan()));
		if (CollectionUtils.isNotEmpty(spanTree.getBranches())) {
			for (SpanTree subTree : spanTree.getBranches()) {
				doSaveTraceSpan(subTree);
			}
		}
	}

	private TraceSpan convertAsTraceSpan(Span span) {
		TraceSpan traceSpan = new TraceSpan();
		traceSpan.setId(idGenerator.generateId());
		PropertyUtils.copyProperties(span, traceSpan, null, false);
		return traceSpan;
	}

	public ResultSetSlice<TraceSpan> search(SearchFilterQuery query) {
		return new TraceSpanQueryResultSetSlice(elasticsearchRestTemplate, query);
	}

	public PageResult<TraceSpanVO> searchForPage(SearchFilterQuery query) {
		ResultSetSlice<TraceSpan> resultSetSlice = search(query);
		PageResponse<TraceSpan> response = resultSetSlice.list(PageRequest.of(query.getPage(), query.getSize()));
		List<TraceSpan> content = response.getContent();
		List<TraceSpanVO> results = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(content)) {
			content.forEach(data -> {
				TraceSpanVO vo = new TraceSpanVO();
				BeanUtils.copyProperties(data, vo);
				results.add(vo);
			});
		}
		return new PageResult<TraceSpanVO>(query.getPage(), query.getSize(), results);
	}

	@Override
	public boolean execute() throws Throwable {
		if (logQueue.isEmpty()) {
			return true;
		}
		List<IndexQuery> queries = new ArrayList<IndexQuery>();
		try {
			for (TraceSpan traceSpan : logQueue) {
				IndexQuery indexQuery = new IndexQuery();
				indexQuery.setId(String.valueOf(traceSpan.getId()));
				indexQuery.setSource(JacksonUtils.toJsonString(traceSpan));
				queries.add(indexQuery);
				logQueue.remove(traceSpan);
			}
			elasticsearchRestTemplate.bulkIndex(queries, IndexCoordinates.of(INDEX_NAME_TRACE_SPAN));
			elasticsearchRestTemplate.indexOps(IndexCoordinates.of(INDEX_NAME_TRACE_SPAN)).refresh();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info("Batch save {} logEntries.", queries.size());
			queries.clear();
			queries = null;
		}
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (timer == null) {
			timer = ThreadUtils.scheduleWithFixedDelay(this, 3, TimeUnit.SECONDS);
		}
	}

	@Override
	public void destroy() throws Exception {
		if (timer != null) {
			timer.cancel();
		}
	}

}
