package com.github.paganini2008.springplayer.crumb.entity;

import static com.github.paganini2008.springplayer.crumb.CrumbConstants.DEFAULT_INDEX_TYPE;
import static com.github.paganini2008.springplayer.crumb.CrumbConstants.INDEX_NAME_TRACE_SPAN;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TraceSpan
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@Document(indexName = INDEX_NAME_TRACE_SPAN, type = DEFAULT_INDEX_TYPE, refreshInterval = "10s")
public class TraceSpan {

	@Id
	@Field(type = FieldType.Long, store = true)
	private Long id;

	@Field(type = FieldType.Keyword, store = true)
	private String traceId;

	@Field(type = FieldType.Long, store = true)
	private Long spanId;

	@Field(type = FieldType.Keyword, store = true)
	private String path;

	@Field(type = FieldType.Long, store = true)
	private Long timestamp;

	@Field(type = FieldType.Long, store = true)
	private Long elapsed;

	@Field(type = FieldType.Integer, store = true)
	private Integer status;

	@Field(type = FieldType.Integer, store = true)
	private Integer concurrents;

	@Field(type = FieldType.Long, store = true)
	private Long parentSpanId;

}
