package com.github.paganini2008.springplayer.messenger.entity;

import java.util.Map;

import com.github.paganini2008.springplayer.email.RichTextType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * EmailEntity
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ApiModel("邮件请求实体")
@Getter
@Setter
public class EmailEntity {

	@ApiModelProperty("邮件标题")
	private String subject;
	
	@ApiModelProperty("发送方")
	private String from;
	
	@ApiModelProperty("接收方")
	private String[] to;
	
	@ApiModelProperty("密送方")
	private String[] bcc;
	
	@ApiModelProperty("抄送方")
	private String[] cc;
	
	@ApiModelProperty("回复方")
	private String replyTo;

	@ApiModelProperty("邮件模板")
	private String template;
	
	@ApiModelProperty("邮件模板变量")
	private Map<String, Object> variables;
	
	@ApiModelProperty("邮件模板类型")
	private Integer type;

	public RichTextType getType() {
		return type != null ? RichTextType.valueOf(type) : null;
	}

}
