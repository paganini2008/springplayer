package com.github.paganini2008.springplayer.crumb.service;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.es.MatchAllResultSetSlice;
import com.github.paganini2008.springplayer.crumb.entity.TraceSpan;

/**
 * 
 * TraceSpanQueryResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class TraceSpanQueryResultSetSlice extends MatchAllResultSetSlice<TraceSpan> {

	public TraceSpanQueryResultSetSlice(ElasticsearchRestTemplate elasticsearchTemplate, SearchFilterQuery query) {
		super(elasticsearchTemplate, TraceSpan.class);
		this.query = query;
	}

	private final SearchFilterQuery query;

	@Override
	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
		super.configureQuery(queryBuilder);

		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		if (StringUtils.isNotBlank(query.getPath())) {
			bqb.filter(QueryBuilders.termQuery("path", query.getPath()));
		}
		if (StringUtils.isNotBlank(query.getTraceId())) {
			bqb.filter(QueryBuilders.termQuery("traceId", query.getTraceId()));
		}
		if (query.getSpanId() != null) {
			bqb.filter(QueryBuilders.termQuery("spanId", query.getSpanId()));
		}
		if (query.getParentSpanId() != null) {
			bqb.filter(QueryBuilders.termQuery("parentSpanId", query.getParentSpanId()));
		}

		queryBuilder.withFilter(bqb);

		queryBuilder.withSort(SortBuilders.fieldSort("timestamp").order(SortOrder.DESC));
		queryBuilder.withSort(SortBuilders.fieldSort("traceId").order(SortOrder.ASC));
		queryBuilder.withSort(SortBuilders.fieldSort("spanId").order(SortOrder.ASC));
	}

}
