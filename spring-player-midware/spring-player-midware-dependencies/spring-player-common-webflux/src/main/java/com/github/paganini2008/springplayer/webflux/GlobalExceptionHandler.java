package com.github.paganini2008.springplayer.webflux;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.github.paganini2008.devtools.ExceptionUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;
import com.github.paganini2008.springplayer.common.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GlobalExceptionHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Order(200)
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private ApiExceptionContext context;

	@ExceptionHandler(Throwable.class)
	public ApiResult<String> handleException(ServerWebExchange exchange, Throwable e) {
		if (log.isErrorEnabled()) {
			log.error(e.getMessage(), e);
		}
		return getMessageBody(exchange, e);
	}

	private ApiResult<String> getMessageBody(ServerWebExchange exchange, Throwable e) {
		if (e == null) {
			return ApiResult.failed("未知异常");
		}

		String path = exchange.getRequest().getPath().pathWithinApplication().value();
		ThrowableInfo throwableInfo = new ThrowableInfo(path, e.getMessage(), ExceptionUtils.toArray(e), LocalDateTime.now());
		context.getExceptionTraces().add(throwableInfo);

		if (e instanceof ExceptionDescriptor) {
			ExceptionDescriptor descriptor = (ExceptionDescriptor) e;
			ErrorCode errorCode = descriptor.getErrorCode();
			String lang = exchange.getRequest().getHeaders().getFirst("lang");
			return ApiResult.failed(I18nUtils.getErrorMessage(lang, errorCode));
		} else {
			return ApiResult.failed(e.getMessage());
		}
	}

}
