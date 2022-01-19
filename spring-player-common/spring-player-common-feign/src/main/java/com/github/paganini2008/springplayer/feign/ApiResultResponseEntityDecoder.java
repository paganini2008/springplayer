package com.github.paganini2008.springplayer.feign;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;

import com.github.paganini2008.springplayer.common.ApiResult;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;

/**
 * 
 * ApiResultResponseEntityDecoder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class ApiResultResponseEntityDecoder extends ResponseEntityDecoder {

	ApiResultResponseEntityDecoder(Decoder decoder) {
		super(decoder);
	}

	public Object decode(Response response, Type type) throws IOException, FeignException {
		Object result = super.decode(response, type);
		if (result instanceof ApiResult) {
		}
		return result;
	}

}
