package com.github.paganini2008.springplayer.common.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.jdbc.Countable;
import com.github.paganini2008.devtools.jdbc.PageRequest;
import com.github.paganini2008.devtools.jdbc.PageResponse;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * ForwardOnlyResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class ForwardOnlyResultSetSlice<T> implements ResultSetSlice<T> {

	private final EnhancedElasticsearchRestTemplate template;
	private final Class<T> entityClass;

	public ForwardOnlyResultSetSlice(EnhancedElasticsearchRestTemplate template, Class<T> entityClass) {
		this.template = template;
		this.entityClass = entityClass;
	}

	public PageResponse<T> list(PageRequest pageRequest) {
		return new ForwardOnlyPageResponse<T>(pageRequest, this);
	}

	public PageResponse<T> list(PageRequest pageRequest, int maxResults) {
		return new ForwardOnlyPageResponse<T>(pageRequest, this, () -> maxResults);
	}

	public PageResponse<T> list(PageRequest pageRequest, Countable countable) {
		return new ForwardOnlyPageResponse<T>(pageRequest, this, countable);
	}

	private String[] sortedFields = new String[] { "id" };
	private Object[] sortedValues;

	public void setSortedFields(String[] sortedFields) {
		if (sortedFields != null) {
			this.sortedFields = sortedFields;
		}
	}

	@Override
	public List<T> list(int maxResults, int firstResult) {
		List<T> results = new ArrayList<>();
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		configureQuery(searchQueryBuilder);

		for (String sortedField : sortedFields) {
			searchQueryBuilder.withSort(SortBuilders.fieldSort(sortedField).order(SortOrder.ASC));
		}

		SearchHits<T> hits;
		if (ArrayUtils.isNotEmpty(sortedValues)) {
			hits = template.searchAfter(searchQueryBuilder.build(), sortedValues, entityClass,
					template.getIndexCoordinatesFor(entityClass));
		} else {
			hits = template.search(searchQueryBuilder.build(), entityClass, template.getIndexCoordinatesFor(entityClass));
		}
		SearchHit<T> last = null;
		for (SearchHit<T> searchHit : hits.getSearchHits()) {
			T result = handleContent(searchHit);
			results.add(result);
			last = searchHit;
		}
		if (last != null) {
			sortedValues = last.getSortValues().toArray();
		}
		return results;
	}

	@Override
	public int rowCount() {
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		configureQuery(searchQueryBuilder);
		return (int) template.count(searchQueryBuilder.build(), entityClass);
	}

	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
	}

	protected T handleContent(SearchHit<T> searchHit) {
		return searchHit.getContent();
	}

}
