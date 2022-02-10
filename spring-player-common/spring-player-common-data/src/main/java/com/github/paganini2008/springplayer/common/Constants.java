package com.github.paganini2008.springplayer.common;

/**
 * 
 * Constants
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class Constants {

	public static final String TENANT_ID = "__tenantId__";
	public static final String REQUEST_HEADER_TIMESTAMP = "__timestamp__";
	public static final String REQUEST_HEADER_TRACES = "__traces__";
	public static final String REQUEST_HEADER_TRACE_ID = "__traceId__";
	public static final String REQUEST_HEADER_SPAN_ID = "__spanId__";
	public static final String REQUEST_HEADER_PARENT_SPAN_ID = "__parentSpanId__";

	public static final String REQUEST_PATH = "__requestPath__";

	public static final String KAFKA_TOPIC_APPLOG = "applog";
	public static final String INDEX_NAME_LOG_COLLECTOR = "LogCollector";
	
	public static final String SENTINEL_GATEWAY_RULE = "rule";
	public static final String SENTINEL_GATEWAY_RULE_API = "api";

}
