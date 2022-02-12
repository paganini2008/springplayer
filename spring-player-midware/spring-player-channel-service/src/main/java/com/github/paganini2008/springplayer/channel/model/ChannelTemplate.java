package com.github.paganini2008.springplayer.channel.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ChannelTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("infra_channel_template")
@Getter
@Setter
public class ChannelTemplate {

	@TableId
	private Long id;

	@TableField("channel_id")
	private Long channelId;

	@TableField("template_id")
	private Long templateId;

}
