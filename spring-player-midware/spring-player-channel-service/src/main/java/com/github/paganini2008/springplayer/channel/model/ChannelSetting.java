package com.github.paganini2008.springplayer.channel.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ChannelSetting
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("infra_channel_settings")
@Getter
@Setter
public class ChannelSetting {

	@TableId
	private Long id;

	@TableField("name")
	private String name;
	
	@TableField("value")
	private String value;
	
	@TableField("channel_id")
	private Long channelId;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;
	
}
