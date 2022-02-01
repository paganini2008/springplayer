package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.paganini2008.devtools.ExceptionUtils;
import com.github.paganini2008.devtools.LocaleUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.BizException;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.web.HttpRequestContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GlobalExceptionHandler
 * 
 * @author Fred Feng
 *
 * @since 1.0
 */
@Slf4j
@Order(200)
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private ApiExceptionContext ctx;

	@Autowired
	private MessageLocalization messageLocalization;

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(BizException.class)
	public ResponseEntity<ApiResult<?>> handleBizException(HttpServletRequest request, BizException e) throws Exception {
		if (e != null) {
			log.error(e.getMessage(), e);
			ctx.getExceptionTraces()
					.add(new ThrowableInfo(request.getServletPath(), e.getMessage(), ExceptionUtils.toArray(e), LocalDateTime.now()));
		}

		ApiResult<Object> result = ApiResult.failed(getErrorMessage(e.getErrorCode()));
		result.setRequestPath(request.getServletPath());

		String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isNotBlank(timestamp)) {
			result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
		}
		return new ResponseEntity<ApiResult<?>>(result, e.getHttpStatus());
	}

	private String getErrorMessage(ErrorCode errorCode) {
		Locale locale;
		String lang = HttpRequestContextHolder.getHeader("lang");
		if (StringUtils.isNotBlank(lang)) {
			locale = LocaleUtils.getLocale(lang);
		} else {
			locale = LocaleContextHolder.getLocale();
		}
		return messageLocalization.getMessage(errorCode, locale);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResult<?>> handleException(HttpServletRequest request, Exception e) throws Exception {
		if (e != null) {
			log.error(e.getMessage(), e);
			ctx.getExceptionTraces()
					.add(new ThrowableInfo(request.getServletPath(), e.getMessage(), ExceptionUtils.toArray(e), LocalDateTime.now()));
		}

		ApiResult<Object> result = ApiResult.failed("系统内部错误: " + e.getMessage());
		result.setRequestPath(request.getServletPath());
		String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isNotBlank(timestamp)) {
			result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
		}
		return new ResponseEntity<ApiResult<?>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}