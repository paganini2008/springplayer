package com.github.paganini2008.springplayer.applog.service;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.applog.entity.AppLog;
import com.github.paganini2008.springplayer.common.es.MatchAllResultSetSlice;

/**
 * 
 * AppLogQueryResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class AppLogQueryResultSetSlice extends MatchAllResultSetSlice<AppLog> {

	public AppLogQueryResultSetSlice(ElasticsearchRestTemplate elasticsearchTemplate, SearchFilterQuery query) {
		super(elasticsearchTemplate, AppLog.class);
		this.query = query;
	}

	private final SearchFilterQuery query;

	@Override
	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
		super.configureQuery(queryBuilder);

		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		if (StringUtils.isNotBlank(query.getClusterName())) {
			bqb.filter(QueryBuilders.termQuery("clusterName", query.getClusterName()));
		}
		if (StringUtils.isNotBlank(query.getApplicationName())) {
			bqb.filter(QueryBuilders.termQuery("applicationName", query.getApplicationName()));
		}
		if (StringUtils.isNotBlank(query.getHost())) {
			bqb.filter(QueryBuilders.termQuery("host", query.getHost()));
		}
		if (StringUtils.isNotBlank(query.getIdentifier())) {
			bqb.filter(QueryBuilders.termQuery("identifier", query.getIdentifier()));
		}
		if (StringUtils.isNotBlank(query.getTraceId())) {
			bqb.filter(QueryBuilders.termQuery("traceId", query.getTraceId()));
		}
		if (StringUtils.isNotBlank(query.getLoggerName())) {
			bqb.filter(QueryBuilders.termQuery("loggerName", query.getLoggerName()));
		}
		if (StringUtils.isNotBlank(query.getLevel())) {
			bqb.filter(QueryBuilders.termQuery("level", query.getLevel()));
		}
		if (StringUtils.isNotBlank(query.getMarker())) {
			bqb.filter(QueryBuilders.termQuery("marker", query.getMarker()));
		}

		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("createTime");
		long startTime = query.getStartTime() != null ? query.getStartTime().getTime() : 0;
		long endTime = query.getEndTime() != null ? query.getEndTime().getTime() : 0;
		if (startTime == 0 && endTime == 0) {
			endTime = System.currentTimeMillis();
		}
		if (startTime > 0) {
			rangeQuery.gte(startTime);
		}
		if (endTime > 0) {
			rangeQuery.lte(endTime);
		}
		bqb.filter(rangeQuery);

		queryBuilder.withFilter(bqb);

		queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
	}

}
