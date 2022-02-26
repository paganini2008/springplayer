package com.github.paganini2008.springplayer.es;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

/**
 * 
 * MatchAllQueryResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MatchAllQueryResultSetSlice<T> extends AbstractQueryResultSetSlice<T> {

	public MatchAllQueryResultSetSlice(ElasticsearchRestTemplate elasticsearchTemplate, Class<T> entityClass) {
		super(elasticsearchTemplate, entityClass);
	}

	@Override
	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
		super.configureQuery(queryBuilder);
		queryBuilder.withQuery(QueryBuilders.matchAllQuery());
	}

}
