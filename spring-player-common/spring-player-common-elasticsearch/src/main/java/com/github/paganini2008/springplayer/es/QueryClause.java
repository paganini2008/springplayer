package com.github.paganini2008.springplayer.es;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 
 * QueryClause
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum QueryClause {

	SHOULD {

		@Override
		BoolQueryBuilder matchQuery(BoolQueryBuilder queryBuilder, String searchField, String keyword) {
			return queryBuilder.should(QueryBuilders.matchQuery(searchField, keyword));
		}

	},
	MUST {

		@Override
		BoolQueryBuilder matchQuery(BoolQueryBuilder queryBuilder, String searchField, String keyword) {
			return queryBuilder.must(QueryBuilders.matchQuery(searchField, keyword));
		}

	},
	MUST_NOT {

		@Override
		BoolQueryBuilder matchQuery(BoolQueryBuilder queryBuilder, String searchField, String keyword) {
			return queryBuilder.mustNot(QueryBuilders.matchQuery(searchField, keyword));
		}

	};

	abstract BoolQueryBuilder matchQuery(BoolQueryBuilder queryBuilder, String searchField, String keyword);

}
