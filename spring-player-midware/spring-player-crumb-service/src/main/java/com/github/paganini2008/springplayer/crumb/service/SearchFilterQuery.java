package com.github.paganini2008.springplayer.crumb.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.github.paganini2008.springplayer.common.PageQuery;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * SearchQuery
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class SearchFilterQuery extends PageQuery {

	private String traceId;
	private Long spanId;
	private String path;
	private Long parentSpanId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
