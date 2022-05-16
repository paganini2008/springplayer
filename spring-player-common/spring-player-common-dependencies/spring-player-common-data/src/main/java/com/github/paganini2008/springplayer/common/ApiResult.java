package com.github.paganini2008.springplayer.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ApiResult
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class ApiResult<T> {

	private int code;
	private String msg;
	private T data;
	private long elapsed;
	private String requestPath;

	public static <T> ApiResult<T> ok() {
		return ok("操作成功", null);
	}

	public static <T> ApiResult<T> ok(T data) {
		return ok("操作成功", data);
	}

	public static <T> ApiResult<T> ok(String msg, T data) {
		ApiResult<T> rs = new ApiResult<T>();
		rs.setCode(1);
		rs.setMsg(msg);
		rs.setData(data);
		return rs;
	}

	public static <T> ApiResult<T> failed() {
		return failed("操作失败");
	}

	public static <T> ApiResult<T> failed(String msg) {
		return failed(msg, null);
	}

	public static <T> ApiResult<T> failed(String msg, T data) {
		ApiResult<T> rs = new ApiResult<T>();
		rs.setCode(0);
		rs.setMsg(msg);
		rs.setData(data);
		return rs;
	}

}
