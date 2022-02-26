package com.github.paganini2008.springplayer.feign;

import org.springframework.http.HttpStatus;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.BizException;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.JacksonUtils;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

/**
 * 
 * GlobalFeignErrorDecoder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class GlobalFeignErrorDecoder extends ErrorDecoder.Default {

	@Override
	public Exception decode(String methodKey, Response response) {
		Exception e = super.decode(methodKey, response);
		if (e instanceof RetryableException) {
			return e;
		}
		if (e instanceof FeignException && ((FeignException) e).responseBody().isPresent()) {
			FeignException fe = (FeignException) e;
			String responseBody = fe.contentUTF8();
			ApiResult<?> result = null;
			try {
				result = JacksonUtils.parseJson(responseBody, ApiResult.class);
			} catch (RuntimeException ignored) {
			}
			return new BizException(ErrorCode.feignClientError(result != null ? result.getMsg() : responseBody),
					HttpStatus.valueOf(fe.status()), fe);
		}
		return e;
	}

}
