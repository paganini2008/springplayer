package com.github.paganini2008.springplayer.webflux;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.i18n.I18nUtils;

/**
 * 
 * ValidationExceptionHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Order(100)
@RestControllerAdvice
public class ValidationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(WebExchangeBindException.class)
	public ApiResult<?> handleException(WebExchangeBindException exception) {
		List<FieldError> fieldErrors = exception.getFieldErrors();
		if (CollectionUtils.isEmpty(fieldErrors)) {
			return ApiResult.failed();
		}
		String lang = RequestHeaderContextHolder.getHeader("lang");
		String message = fieldErrors.get(0).getDefaultMessage();
		return ApiResult.failed(getI18nMessage(lang, message));
	}

	private String getI18nMessage(String lang, String repr) {
		String[] args = repr.split(":", 2);
		String messageKey, defaultMessage;
		if (args.length == 1) {
			messageKey = args[0];
			defaultMessage = args[0];
		} else {
			messageKey = args[0];
			defaultMessage = args[1];
		}
		String message = I18nUtils.getMessage(lang, messageKey);
		if (StringUtils.isBlank(message)) {
			message = defaultMessage;
		}
		return message;
	}

}
