package com.github.paganini2008.springplayer.channel.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.paganini2008.springplayer.channel.enums.ChannelType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Channel
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@TableName("infra_channel")
@Getter
@Setter
public class Channel {

	@TableId
	private Long id;

	@TableField("name")
	private String name;

	@TableField("channel_type")
	private ChannelType channelType;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
