package com.github.paganini2008.springplayer.channel.pojo;

import java.time.LocalDateTime;

import com.github.paganini2008.springplayer.channel.enums.ChannelType;

import lombok.Data;

/**
 * 
 * ChannelVO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class ChannelVO {

	private Long id;
	private String name;
	private ChannelType channelType;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

}
