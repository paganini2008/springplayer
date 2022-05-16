package com.github.paganini2008.springplayer.common.messenger.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * PopupDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ApiModel("弹窗请求实体")
@Getter
@Setter
@ToString
public class PopupDTO {

	@ApiModelProperty("WS地址")
	private String wsUrl;

	@ApiModelProperty("消息模板")
	private String template;
	
	@ApiModelProperty("模板变量")
	private Map<String, Object> variables;

}
