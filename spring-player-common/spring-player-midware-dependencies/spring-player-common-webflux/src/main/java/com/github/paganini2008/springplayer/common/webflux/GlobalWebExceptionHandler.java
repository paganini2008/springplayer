package com.github.paganini2008.springplayer.common.webflux;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.github.paganini2008.devtools.ExceptionUtils;
import com.github.paganini2008.devtools.LocaleUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;
import com.github.paganini2008.springplayer.common.context.MessageLocalization;

import reactor.core.publisher.Mono;

/**
 * 
 * GlobalWebExceptionHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class GlobalWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public GlobalWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
			ErrorProperties errorProperties, ApplicationContext applicationContext, ApiExceptionContext apiExceptionContext,
			MessageLocalization messageLocalization) {
		super(errorAttributes, resourceProperties, errorProperties, applicationContext);
		this.apiExceptionContext = apiExceptionContext;
		this.messageLocalization = messageLocalization;
	}

	private final ApiExceptionContext apiExceptionContext;
	private final MessageLocalization messageLocalization;

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		boolean includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
		Map<String, Object> error = getErrorAttributes(request,
				(includeStackTrace) ? ErrorAttributeOptions.of(Include.STACK_TRACE) : ErrorAttributeOptions.defaults());
		Throwable e = getError(request);
		if (e != null) {
			apiExceptionContext.getExceptionTraces()
					.add(new ThrowableInfo(request.uri().getRawPath(), e.getMessage(), ExceptionUtils.toArray(e), LocalDateTime.now()));
		}
		return ServerResponse.status(super.getHttpStatus(error)).contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(getMessageBody(request, e)));
	}

	private ApiResult<String> getMessageBody(ServerRequest request, Throwable e) {
		ApiResult<String> result;
		if (e instanceof ExceptionDescriptor) {
			ExceptionDescriptor descriptor = (ExceptionDescriptor) e;
			ErrorCode errorCode = descriptor.getErrorCode();
			String lang = request.headers().firstHeader("lang");
			result = ApiResult.failed(messageLocalization.getMessage(errorCode, LocaleUtils.toLocale(lang)));
		} else {
			result = ApiResult.failed(e.getMessage());
		}
		result.setRequestPath(request.uri().getRawPath());

		if (StringUtils.isNotBlank(request.headers().firstHeader(REQUEST_HEADER_TIMESTAMP))) {
			long timestamp = Long.parseLong(request.headers().firstHeader(REQUEST_HEADER_TIMESTAMP));
			result.setElapsed(System.currentTimeMillis() - timestamp);
		}
		return result;
	}

}
