package com.github.paganini2008.springplayer.es;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

/**
 * 
 * ForwardOnlyMatchAllResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class ForwardOnlyMatchAllResultSetSlice<T> extends ForwardOnlyResultSetSlice<T> {

	public ForwardOnlyMatchAllResultSetSlice(EnhancedElasticsearchRestTemplate template, Class<T> entityClass) {
		super(template, entityClass);
	}

	@Override
	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
		super.configureQuery(queryBuilder);
		queryBuilder.withQuery(QueryBuilders.matchAllQuery());
	}

}
