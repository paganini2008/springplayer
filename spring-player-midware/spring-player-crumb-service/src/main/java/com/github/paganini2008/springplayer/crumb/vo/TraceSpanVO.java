package com.github.paganini2008.springplayer.crumb.vo;

import com.github.paganini2008.devtools.time.DateUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TraceSpanVO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class TraceSpanVO {

	private String traceId;
	private Long spanId;
	private String path;
	private Long timestamp;
	private Long elapsed;
	private Integer status;
	private Integer concurrents;
	private Long parentSpanId;

	public String getCreateTime() {
		return timestamp != null && timestamp > 0 ? DateUtils.format(timestamp, "yyyy-MM-dd HH:mm:ss") : "";
	}

}
