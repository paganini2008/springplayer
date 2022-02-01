package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.LocaleUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.web.HttpRequestContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GlobalErrorController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RestController
public class GlobalErrorController extends AbstractErrorController {

	private static final String ERROR_PATH = "/error";

	@Autowired
	public GlobalErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@Autowired
	private MessageLocalization messageLocalization;

	@RequestMapping(value = ERROR_PATH, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResult<Object>> error(HttpServletRequest request, HttpServletResponse response) {
		final Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.of(Include.STACK_TRACE));
		if (log.isErrorEnabled()) {
			log.error("ErrorAttributes: " + errorAttributes.toString());
		}
		
		ErrorCode errorCode = (ErrorCode) errorAttributes.get("errorCode");
		HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
		String message = "";
		if (errorCode != null) {
			message = getErrorMessage(errorCode);
		} else {
			message = (String) errorAttributes.get("message");
			if (StringUtils.isBlank(message) && httpStatus.isError()) {
				message = (String) errorAttributes.getOrDefault("error", "内部系统异常");
			}
		}
		
		ApiResult<Object> result = ApiResult.failed(message);
		result.setRequestPath((String) errorAttributes.getOrDefault("path", request.getServletPath()));
		String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isNotBlank(timestamp)) {
			result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
		}
		return new ResponseEntity<ApiResult<Object>>(result, httpStatus);
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
}
