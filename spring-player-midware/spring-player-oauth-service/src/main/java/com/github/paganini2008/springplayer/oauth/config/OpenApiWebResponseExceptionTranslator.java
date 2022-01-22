
package com.github.paganini2008.springplayer.oauth.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.security.ErrorCodes;
import com.github.paganini2008.springplayer.security.OpenApiException;

/**
 * 
 * OpenApiWebResponseExceptionTranslator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class OpenApiWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception cause) {
		Throwable[] causeChain = throwableAnalyzer.determineCauseChain(cause);

		Exception e = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
		if (e != null) {
			return handleException(e, ErrorCodes.UNAUTHORIZED, 401);
		}

		e = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
		if (e != null) {
			return handleException(e, ErrorCodes.FORBIDDEN, 403);
		}
		e = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class,
				causeChain);
		if (e != null) {
			return handleException(e, ErrorCodes.INVALID, 405);
		}
		e = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class, causeChain);
		if (e != null) {
			return handleOAuth2Exception((OAuth2Exception) e, ErrorCodes.BAD_CREDENTIALS);
		}
		e = (ClientAuthenticationException) throwableAnalyzer.getFirstThrowableOfType(ClientAuthenticationException.class, causeChain);
		if (e != null) {
			return handleOAuth2Exception((OAuth2Exception) e, ErrorCodes.BAD_CLIENT_CREDENTIALS);
		}
		e = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
		if (e != null) {
			return handleOAuth2Exception((OAuth2Exception) e, ErrorCodes.UNAUTHORIZED);
		}
		e = (Exception) throwableAnalyzer.getFirstThrowableOfType(Exception.class, causeChain);
		if (e != null) {
			return handleException(e, ErrorCode.internalServerError(e.getMessage()), 500);
		}
		return handleException(new Exception(), ErrorCode.internalServerError("internal server error"), 500);
	}

	private ResponseEntity<OAuth2Exception> handleException(Exception e, ErrorCode errorCode, int statusCode) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(new OpenApiException(e.getMessage(), errorCode),
				headers, httpStatus);
		return response;
	}

	private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e, ErrorCode errorCode) {
		int statusCode = e.getHttpErrorCode();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		if (statusCode == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
			headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
		}
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(new OpenApiException(e.getMessage(), errorCode),
				headers, httpStatus);
		return response;

	}

}
