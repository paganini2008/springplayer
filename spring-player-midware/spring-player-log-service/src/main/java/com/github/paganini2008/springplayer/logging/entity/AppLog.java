package com.github.paganini2008.springplayer.logging.entity;

import static com.github.paganini2008.springplayer.logging.LoggingConstants.DEFAULT_INDEX_NAME;
import static com.github.paganini2008.springplayer.logging.LoggingConstants.DEFAULT_INDEX_TYPE;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * AppLog
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@Document(indexName = DEFAULT_INDEX_NAME, type = DEFAULT_INDEX_TYPE, refreshInterval = "10s")
public class AppLog {

	@Id
	@Field(type = FieldType.Long, store = true)
	private Long id;

	@Field(type = FieldType.Keyword, store = true)
	private String clusterName;

	@Field(type = FieldType.Keyword, store = true)
	private String applicationName;

	@Field(type = FieldType.Keyword, store = true)
	private String host;

	@Field(type = FieldType.Keyword, store = true)
	private String identifier;

	@Field(type = FieldType.Keyword, store = true)
	private String traceId;

	@Field(type = FieldType.Integer, store = true)
	private Integer span;

	@Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
	private String args;

	@Field(type = FieldType.Keyword, store = true)
	private String loggerName;

	@Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
	private String message;

	@Field(type = FieldType.Keyword, store = true)
	private String level;

	@Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
	private String error;

	@Field(type = FieldType.Keyword, store = true)
	private String marker;

	@Field(type = FieldType.Long, store = true)
	private long createTime;

}
