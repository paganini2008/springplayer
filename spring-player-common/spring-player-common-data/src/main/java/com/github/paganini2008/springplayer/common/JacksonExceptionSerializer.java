package com.github.paganini2008.springplayer.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.SneakyThrows;

/**
 * 
 * JacksonExceptionSerializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class JacksonExceptionSerializer extends StdSerializer<ExceptionDescriptor>{

	private static final long serialVersionUID = 4369712924277867480L;

	public JacksonExceptionSerializer() {
		super(ExceptionDescriptor.class);
	}

	@Override
	@SneakyThrows
	public void serialize(ExceptionDescriptor e, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", 0);
		gen.writeStringField("msg", e.getMessage());
		gen.writeStringField("data", getI18nMessage(e.getErrorCode()));
		gen.writeStringField("requestPath", "");
		gen.writeNumberField("elapsed", 0L);
		gen.writeEndObject();
	}
	
	protected String getI18nMessage(ErrorCode errorCode) {
		return errorCode.getDefaultMessage();
	}

}
