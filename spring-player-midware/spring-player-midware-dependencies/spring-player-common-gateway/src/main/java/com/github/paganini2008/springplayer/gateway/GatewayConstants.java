package com.github.paganini2008.springplayer.gateway;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * GatewayConstants
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class GatewayConstants {

	/** 网关参数 **/
	public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";
	public static final String HTTP_REQUEST_TRACE = "httpRequestTrace";

	public static final String REDIS_PUBSUB_CHANNEL = "yl:platform:redis:pubsub:sentinel-gateway";

	/** 签名和验签 **/
	public static final String SIGN_KEY_APPID = "AppId";
	public static final String SIGN_KEY_TIMESTAMP = "timestamp";
	public static final String SIGN_KEY = "signature";

	/** 网关路由管理 **/
	public static final String REDIS_KEY_ROUTE = "yl:platform:gateway:route";
	public static final String REDIS_PUBSUB_CHANNEL_ROUTE_PUBLISH = "yl:platform:gateway:route:publish";

	/** 版本控制 **/
	public static final String HEADER_VERSION = "VERSION";
	public static final String REQUEST_HEADER_API_VERSION = "API_VERSION";

	/** 流控规则 **/
	public static final String SENTINEL_GATEWAY_FLOW_RULE = "yl:platform:gateway:sentinel:flow";
	public static final String SENTINEL_RULE_PUBLISH_CHANNEL = "yl:platform:sentinel:rule:publish";
	public static final String SENTINEL_RULE_UPDATE_CHANNEL = "yl:platform:sentinel:rule:update";
	
	public static final String[] GENERIC_DATETIME_PATTERNS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss" };

	static Map<String, String> defaultSettings() {
		Map<String, String> settings = new HashMap<>();

		settings.put("reactor.netty.pool.leasingStrategy", "lifo");
		settings.put("eureka.client.registry-fetch-interval-seconds", "60");
		settings.put("management.endpoint.gateway.enabled", "true");
		// settings.put("management.server.port", "8070");
		settings.put("management.endpoints.web.exposure.include", "*");
		settings.put("management.endpoint.health.show-details", "always");

		settings.put("spring.cloud.gateway.metrics.enabled", "true");

		return settings;
	}

}
