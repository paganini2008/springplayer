package com.github.paganini2008.springplayer.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * JacksonUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class JacksonUtils {

	private static final ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

		JavaTimeModule javaTimeModule = new JavaTimeModule();
		mapper.registerModule(javaTimeModule);
	}

	public static byte[] toJsonStringBytes(Object object) {
		if (object == null) {
			return "".getBytes();
		}
		try {
			return mapper.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static String toJsonString(Object object) {
		if (object == null) {
			return "";
		}
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static void writeObject(Writer out, Object object) {
		try {
			mapper.writeValue(out, object);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static void writeObject(OutputStream out, Object object) {
		try {
			mapper.writeValue(out, object);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static <T> T parseJson(byte[] bytes, Class<T> requiredType) {
		if (bytes == null) {
			return null;
		}
		try {
			return mapper.readValue(bytes, requiredType);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static <T> T parseJson(String json, TypeReference<T> typeReference) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, typeReference);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static <T> T parseJson(String json, Class<T> requiredType) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, requiredType);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static void main(String[] args) throws Exception {

	}

}
