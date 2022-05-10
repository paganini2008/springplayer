package com.github.paganini2008.springplayer.common.sentinel;

/**
 * 
 * SentinelConstants
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class SentinelConstants {
	
	public static final String DEFAULT_NAMESPACE_NAME = "sentinel";
	
	public static final String REDIS_PUBSUB_CHANNEL = "yl:platform:redis:pubsub:sentinel";

	/** 流控规则 **/
	public static final String REDIS_PUBSUB_SENTINEL_RULE_PUBLISH = "yl:platform:sentinel:rule:publish";
	public static final String REDIS_PUBSUB_SENTINEL_RULE_UPDATE = "yl:platform:sentinel:rule:update";

}
