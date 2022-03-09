package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.i18n.I18nUtils;
import com.github.paganini2008.springplayer.web.WebUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ValidationExceptionHanlder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class ValidationExceptionHanlder {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResult<?>> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException e)
			throws JsonProcessingException {
		List<ObjectError> errors = e.getBindingResult().getAllErrors();
		ObjectError firstError = errors.get(0);
		String field = ((FieldError) firstError).getField();
		String msg = firstError.getDefaultMessage();
		String lang = WebUtils.getLang(request);
		msg = getI18nMessage(lang, msg);
		log.info("Field: {}, Msg: {}", field, msg);
		ApiResult<?> result = ApiResult.failed(msg);
		result.setRequestPath(request.getServletPath());
		if (StringUtils.isNotBlank(request.getHeader(REQUEST_HEADER_TIMESTAMP))) {
			long timestamp = Long.parseLong(request.getHeader(REQUEST_HEADER_TIMESTAMP));
			result.setElapsed(System.currentTimeMillis() - timestamp);
		}
		return new ResponseEntity<ApiResult<?>>(result, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResult<?>> handleValidationException(HttpServletRequest request, ConstraintViolationException e) {
		List<ConstraintViolation<?>> errors = new ArrayList<ConstraintViolation<?>>(e.getConstraintViolations());
		ConstraintViolation<?> firstError = errors.get(0);
		String path = firstError.getPropertyPath().toString();
		String msg = firstError.getMessage();
		String lang = WebUtils.getLang(request);
		msg = getI18nMessage(lang, msg);
		log.info("Path: {}, Msg: {}", path, msg);
		ApiResult<?> result = ApiResult.failed(msg);
		result.setRequestPath(request.getServletPath());
		if (StringUtils.isNotBlank(request.getHeader(REQUEST_HEADER_TIMESTAMP))) {
			long timestamp = Long.parseLong(request.getHeader(REQUEST_HEADER_TIMESTAMP));
			result.setElapsed(System.currentTimeMillis() - timestamp);
		}
		return new ResponseEntity<ApiResult<?>>(result, HttpStatus.BAD_REQUEST);
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
