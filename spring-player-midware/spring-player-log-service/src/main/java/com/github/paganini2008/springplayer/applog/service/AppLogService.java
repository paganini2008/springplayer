package com.github.paganini2008.springplayer.applog.service;

import static com.github.paganini2008.springplayer.applog.AppLogConstants.INDEX_NAME_APPLOG;

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

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.devtools.beans.PropertyUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.devtools.jdbc.PageRequest;
import com.github.paganini2008.devtools.jdbc.PageResponse;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;
import com.github.paganini2008.devtools.multithreads.Executable;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.springplayer.applog.entity.AppLog;
import com.github.paganini2008.springplayer.applog.vo.AppLogVO;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.PageResult;
import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.logging.common.AppLogEntry;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * AppLogService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
public class AppLogService implements Executable, InitializingBean, DisposableBean {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	private IdGenerator idGenerator;

	private Timer timer;
	private final List<AppLog> logQueue = new CopyOnWriteArrayList<AppLog>();

	public void saveAppLog(AppLogEntry entry) {
		AppLog appLog = new AppLog();
		appLog.setId(idGenerator.generateId());
		appLog.setCreateTime(entry.getCreateTime().getTime());
		PropertyUtils.copyProperties(entry, appLog, null, false);
		logQueue.add(appLog);
	}

	public ResultSetSlice<AppLog> search(String keyword, SearchFilterQuery query) {
		if (StringUtils.isBlank(keyword)) {
			return new AppLogQueryResultSetSlice(elasticsearchRestTemplate, query);
		}
		return new AppLogKeywordQueryResultSetSlice(elasticsearchRestTemplate, keyword, query);
	}

	public PageResult<AppLogVO> searchForPage(String keyword, SearchFilterQuery query) {
		ResultSetSlice<AppLog> resultSetSlice = search(keyword, query);
		PageResponse<AppLog> response = resultSetSlice.list(PageRequest.of(query.getPage(), query.getSize()));
		List<AppLog> content = response.getContent();
		List<AppLogVO> results = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(content)) {
			content.forEach(data -> {
				AppLogVO vo = new AppLogVO();
				BeanUtils.copyProperties(data, vo);
				results.add(vo);
			});
		}
		return new PageResult<AppLogVO>(query.getPage(), query.getSize(), results);
	}

	@Override
	public boolean execute() throws Throwable {
		if (logQueue.isEmpty()) {
			return true;
		}
		List<IndexQuery> queries = new ArrayList<IndexQuery>();
		try {
			for (AppLog applog : logQueue) {
				IndexQuery indexQuery = new IndexQuery();
				indexQuery.setId(String.valueOf(applog.getId()));
				indexQuery.setSource(JacksonUtils.toJsonString(applog));
				queries.add(indexQuery);
				logQueue.remove(applog);
			}
			elasticsearchRestTemplate.bulkIndex(queries, IndexCoordinates.of(INDEX_NAME_APPLOG));
			elasticsearchRestTemplate.indexOps(IndexCoordinates.of(INDEX_NAME_APPLOG)).refresh();
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
