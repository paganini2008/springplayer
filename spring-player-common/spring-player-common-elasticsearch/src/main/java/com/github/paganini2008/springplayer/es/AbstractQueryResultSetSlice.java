package com.github.paganini2008.springplayer.es;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.github.paganini2008.devtools.jdbc.PageableResultSetSlice;

/**
 * 
 * AbstractQueryResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class AbstractQueryResultSetSlice<T> extends PageableResultSetSlice<T> {

	private final ElasticsearchRestTemplate elasticsearchTemplate;
	private final Class<T> entityClass;

	public AbstractQueryResultSetSlice(ElasticsearchRestTemplate elasticsearchTemplate, Class<T> entityClass) {
		super();
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.entityClass = entityClass;
	}

	@Override
	public List<T> list(int maxResults, int firstResult) {
		List<T> results = new ArrayList<>();
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		configureQuery(searchQueryBuilder);
		if (maxResults > 0) {
			searchQueryBuilder.withPageable(PageRequest.of(getPageNumber() - 1, maxResults));
		}
		SearchHits<T> hits = elasticsearchTemplate.search(searchQueryBuilder.build(), entityClass);
		for (SearchHit<T> searchHit : hits.getSearchHits()) {
			T result = handleContent(searchHit);
			results.add(result);
		}
		return results;
	}

	@Override
	public int rowCount() {
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		configureQuery(searchQueryBuilder);
		return (int) elasticsearchTemplate.count(searchQueryBuilder.build(), entityClass);
	}

	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
	}

	protected T handleContent(SearchHit<T> searchHit) {
		return searchHit.getContent();
	}

}
