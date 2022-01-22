package com.github.paganini2008.springplayer.webmvc;

import static com.github.paganini2008.springplayer.common.Constants.TIMESTAMP;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;

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

	@RequestMapping(value = ERROR_PATH, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResult<Object>> error(HttpServletRequest request, HttpServletResponse response) {
		final Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.of(Include.STACK_TRACE));
		log.error("ErrorAttributes: " + errorAttributes.toString());
		HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
		String message = (String) errorAttributes.get("message");
		if (httpStatus.isError() && StringUtils.isBlank(message)) {
			message = (String) errorAttributes.getOrDefault("error", "内部系统异常");
		}
		ApiResult<Object> result = ApiResult.failed(message);
		result.setRequestPath((String) errorAttributes.getOrDefault("path", request.getServletPath()));
		if (StringUtils.isNotBlank(request.getHeader(TIMESTAMP))) {
			long timestamp = Long.parseLong(request.getHeader(TIMESTAMP));
			result.setElapsed(System.currentTimeMillis() - timestamp);
		}
		return new ResponseEntity<ApiResult<Object>>(result, httpStatus);
	}

}
