package com.github.paganini2008.springplayer.common.gateway.monitor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

/**
 * 
 * SimpleLabelExtractor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SimpleLabelExtractor implements LabelExtractor {

	@SuppressWarnings("unchecked")
	@Override
	public Object extractLabel(String labelName, HttpTrace httpTrace) {
		String value = (String) httpTrace.getHeader().get(labelName);
		if (StringUtils.isBlank(value)) {
			if (httpTrace instanceof HttpRequestTrace) {
				String body = ((HttpRequestTrace) httpTrace).getBody();
				if (StringUtils.isNotBlank(body)) {
					try {
						Map<String, Object> requestBody = JacksonUtils.parseJson(body, HashMap.class);
						return requestBody.get(labelName);
					} catch (RuntimeException ignored) {
					}
				}
			}
		}
		return value;
	}

}
