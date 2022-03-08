package com.github.paganini2008.springplayer.es;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

/**
 * 
 * MatchAllResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MatchAllResultSetSlice<T> extends AbstractQueryResultSetSlice<T> {

	public MatchAllResultSetSlice(ElasticsearchRestTemplate elasticsearchTemplate, Class<T> entityClass) {
		super(elasticsearchTemplate, entityClass);
	}

	@Override
	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
		super.configureQuery(queryBuilder);
		queryBuilder.withQuery(QueryBuilders.matchAllQuery());
	}

}
