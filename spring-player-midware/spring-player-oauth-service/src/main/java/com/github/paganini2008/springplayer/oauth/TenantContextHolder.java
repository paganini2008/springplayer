
package com.github.paganini2008.springplayer.oauth;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.github.paganini2008.springplayer.common.Constants;

/**
 * 
 * TenantContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class TenantContextHolder {

	private static final ThreadLocal<Map<String, String>> headers = new TransmittableThreadLocal<>();

	public static String getHeader(String headerName) {
		return getHeader(headerName, "");
	}

	public static String getHeader(String headerName, String defaultValue) {
		return headers.get().getOrDefault(headerName, defaultValue);
	}

	public static Long getTenantId() {
		return Long.valueOf(getHeader(Constants.TENANT_ID, "1"));
	}

	public static void setHeaders(Map<String, String> headerMap) {
		headers.set(headerMap);
	}

	public static void clear() {
		headers.remove();
	}

}
