package com.github.paganini2008.springplayer.applog.vo;

import com.github.paganini2008.devtools.time.DateUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * AppLogVO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class AppLogVO {

	private String clusterName;
	private String applicationName;
	private String host;
	private String identifier;
	private String traceId;
	private Integer span;
	private String args;
	private String loggerName;
	private String message;
	private String level;
	private String error;
	private String marker;
	private Long createTime;

	public String getCreateTime() {
		return createTime != null && createTime > 0 ? DateUtils.format(createTime, "yyyy-MM-dd HH:mm:ss") : "";
	}

}
