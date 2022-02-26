package com.github.paganini2008.springplayer.es;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;

/**
 * 
 * KeywordQueryResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class KeywordQueryResultSetSlice<T> extends AbstractQueryResultSetSlice<T> {

	private final String keyword;
	private final QueryClause queryClause;
	private final String[] searchFields;

	public KeywordQueryResultSetSlice(ElasticsearchRestTemplate elasticsearchTemplate, String keyword, QueryClause queryClause,
			String[] searchFields, Class<T> entityClass) {
		super(elasticsearchTemplate, entityClass);
		this.keyword = keyword;
		this.queryClause = queryClause;
		this.searchFields = searchFields;
	}

	@Override
	protected void configureQuery(NativeSearchQueryBuilder queryBuilder) {
		super.configureQuery(queryBuilder);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (String searchField : searchFields) {
			boolQueryBuilder = queryClause.matchQuery(boolQueryBuilder, searchField, keyword);
		}
		queryBuilder.withQuery(boolQueryBuilder)
				.withHighlightFields(Arrays.stream(searchFields).map(fieldName -> new HighlightBuilder.Field(fieldName))
						.toArray(l -> new HighlightBuilder.Field[l]))
				.withHighlightBuilder(new HighlightBuilder().preTags("<font class=\"keyword\">").postTags("</font>")
						.fragmentSize(Integer.MAX_VALUE).numOfFragments(3));
	}

	@Override
	protected T handleContent(SearchHit<T> searchHit) {
		T result = super.handleContent(searchHit);
		for (String searchField : searchFields) {
			List<String> fragments = searchHit.getHighlightField(searchField);
			if (CollectionUtils.isNotEmpty(fragments)) {
				StringBuilder part = new StringBuilder();
				for (String fragment : fragments) {
					part.append(fragment);
				}
				BeanUtils.setProperty(result, searchField, part);
			}
		}
		return result;
	}
}
