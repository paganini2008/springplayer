package com.github.paganini2008.springplayer.es;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.document.SearchDocumentResponse;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;

/**
 * 
 * EnhancedElasticsearchRestTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class EnhancedElasticsearchRestTemplate extends ElasticsearchRestTemplate {

	public EnhancedElasticsearchRestTemplate(RestHighLevelClient client) {
		super(client);
	}

	public EnhancedElasticsearchRestTemplate(RestHighLevelClient client, ElasticsearchConverter elasticsearchConverter) {
		super(client, elasticsearchConverter);
	}

	public <T> SearchHits<T> searchAfter(Query query, Object[] sortedValues, Class<T> clazz, IndexCoordinates index) {
		SearchRequest searchRequest = getRequestFactory().searchRequest(query, clazz, index);
		searchRequest.source().searchAfter(sortedValues);
		SearchResponse response = execute(client -> client.search(searchRequest, RequestOptions.DEFAULT));
		SearchDocumentResponseCallback<SearchHits<T>> callback = new ReadSearchDocumentResponseCallback<>(clazz, index);
		return callback.doWith(SearchDocumentResponse.from(response));
	}

}
