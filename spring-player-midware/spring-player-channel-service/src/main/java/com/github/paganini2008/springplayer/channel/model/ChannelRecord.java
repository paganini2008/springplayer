package com.github.paganini2008.springplayer.channel.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.paganini2008.springplayer.channel.enums.ChannelType;
import com.github.paganini2008.springplayer.channel.enums.ReachState;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ChannelRecord
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("infra_channel_record")
@Getter
@Setter
public class ChannelRecord {

	@TableId
	private Long id;

	@TableField("channel_id")
	private Long channelId;

	@TableField("channel_type")
	private ChannelType channelType;

	@TableField("template_id")
	private Long templateId;

	@TableField("content")
	private String content;

	@TableField("reach_state")
	private ReachState reachState;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
