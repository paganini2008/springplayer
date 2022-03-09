package com.github.paganini2008.springplayer.common.oauth;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;
import com.github.paganini2008.springplayer.common.SimpleErrorCode;

import feign.FeignException;

/**
 * 
 * ErrorCodes
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class ErrorCodes {

	public static final SimpleErrorCode UNAUTHORIZED = new SimpleErrorCode("UNAUTHORIZED", "1004010", "未经授权的请求");
	public static final SimpleErrorCode FORBIDDEN = new SimpleErrorCode("FORBIDDEN", "1004030", "未经允许的请求");
	public static final SimpleErrorCode INVALID = new SimpleErrorCode("INVALID", "1004050", "无效的请求");
	public static final SimpleErrorCode BAD_CREDENTIALS = new SimpleErrorCode("BAD_CREDENTIALS", "1004001", "用户名不存在或者密码错误");
	public static final SimpleErrorCode BAD_CLIENT_CREDENTIALS = new SimpleErrorCode("BAD_CLIENT_CREDENTIALS", "104002", "客户端信息错误");
	public static final SimpleErrorCode ACCESS_DENIED = new SimpleErrorCode("ACCESS_DENIED", "1004031", "请求已被拒绝");

	public static final SimpleErrorCode ACCOUNT_EXPIRED = new SimpleErrorCode("ACCOUNT_EXPIRED", "1004011", "用户已过期");
	public static final SimpleErrorCode CREDENTIALS_EXPIRED = new SimpleErrorCode("CREDENTIALS_EXPIRED", "1004012", "凭证已过期");
	public static final SimpleErrorCode INSUFFICIENT = new SimpleErrorCode("INSUFFICIENT", "1004013", "凭证已失效");
	public static final SimpleErrorCode ACCOUNT_DISABLED = new SimpleErrorCode("ACCOUNT_DISABLED", "1004014", "用户已被禁用");
	public static final SimpleErrorCode ACCOUNT_LOCKED = new SimpleErrorCode("ACCOUNT_LOCKED", "1004015", "用户已被锁定");
	public static final SimpleErrorCode USER_NOT_FOUND = new SimpleErrorCode("USER_NOT_FOUND", "1004016", "此用户名未被绑定");
	public static final SimpleErrorCode SERVICE_NOT_AVAILABLE = new SimpleErrorCode("SERVICE_NOT_AVAILABLE", "1004017", "认证服务不可用");

	public static ErrorCode matches(AuthenticationException e) {
		if (e instanceof ExceptionDescriptor) {
			return ((ExceptionDescriptor) e).getErrorCode();
		}
		if (e.getCause() instanceof ExceptionDescriptor) {
			return ((ExceptionDescriptor) e.getCause()).getErrorCode();
		}
		if (e.getCause() instanceof FeignException) {
			FeignException fe = (FeignException) (e.getCause());
			return ErrorCode.internalServerError(fe.contentUTF8());
		}
		if (e instanceof AccountExpiredException) {
			return ACCOUNT_EXPIRED;
		} else if (e instanceof CredentialsExpiredException) {
			return CREDENTIALS_EXPIRED;
		} else if (e instanceof InsufficientAuthenticationException) {
			return INSUFFICIENT;
		} else if (e instanceof DisabledException) {
			return ACCOUNT_DISABLED;
		} else if (e instanceof LockedException) {
			return ACCOUNT_LOCKED;
		} else if (e instanceof UsernameNotFoundException) {
			return USER_NOT_FOUND;
		} else if (e instanceof BadCredentialsException) {
			return BAD_CREDENTIALS;
		} else if (e instanceof AuthenticationServiceException) {
			return SERVICE_NOT_AVAILABLE;
		}
		return UNAUTHORIZED;
	}
}
