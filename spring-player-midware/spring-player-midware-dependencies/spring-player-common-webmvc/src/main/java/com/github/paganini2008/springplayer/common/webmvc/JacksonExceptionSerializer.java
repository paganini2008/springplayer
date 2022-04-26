package com.github.paganini2008.springplayer.common.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.paganini2008.devtools.LocaleUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;
import com.github.paganini2008.springplayer.common.context.ApplicationContextUtils;
import com.github.paganini2008.springplayer.common.context.MessageLocalization;
import com.github.paganini2008.springplayer.common.web.HttpRequestContextHolder;

import lombok.SneakyThrows;

/**
 * 
 * JacksonExceptionSerializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class JacksonExceptionSerializer extends StdSerializer<ExceptionDescriptor> {

	private static final long serialVersionUID = 4369712924277867480L;

	public JacksonExceptionSerializer() {
		super(ExceptionDescriptor.class);
	}

	@Override
	@SneakyThrows
	public void serialize(ExceptionDescriptor e, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", 0);
		gen.writeStringField("msg", getI18nMessage(e.getErrorCode()));
		gen.writeStringField("data", null);
		gen.writeStringField("requestPath", "");
		String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
		long elapsed = 0;
		if (StringUtils.isNotBlank(timestamp)) {
			elapsed = System.currentTimeMillis() - Long.parseLong(timestamp);
		}
		gen.writeNumberField("elapsed", elapsed);
		gen.writeEndObject();
	}

	private String getI18nMessage(ErrorCode errorCode) {
		String lang = HttpRequestContextHolder.getHeader("lang");
		Locale locale;
		if (StringUtils.isNotBlank(lang)) {
			locale = LocaleUtils.getLocale(lang);
		} else {
			locale = LocaleContextHolder.getLocale();
		}
		MessageLocalization messageLocalization = ApplicationContextUtils.getBean(MessageLocalization.class);
		return messageLocalization.getMessage(errorCode, locale);
	}

}
