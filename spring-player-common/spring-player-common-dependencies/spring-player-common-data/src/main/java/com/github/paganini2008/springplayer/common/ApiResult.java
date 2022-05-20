package com.github.paganini2008.springplayer.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ApiResult
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ApiModel("统一响应对象")
@Getter
@Setter
public class ApiResult<T> {

	@ApiModelProperty("成功标识，1:成功 0:失败")
	private int code;
	@ApiModelProperty("消息提示")
	private String msg;
	@ApiModelProperty("返回值")
	private T data;
	@ApiModelProperty("响应耗时")
	private long elapsed;
	@ApiModelProperty("接口路径")
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
