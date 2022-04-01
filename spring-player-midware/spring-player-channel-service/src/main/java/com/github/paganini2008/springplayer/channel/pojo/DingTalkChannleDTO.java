package com.github.paganini2008.springplayer.channel.pojo;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DingTalkChannleDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Setter
@Getter
public class DingTalkChannleDTO extends ChannelDTO{

	@NotBlank(message = "服务地址不能为空")
	private String serviceUrl;

}
