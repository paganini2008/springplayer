package com.github.paganini2008.springplayer.channel.pojo;

import com.github.paganini2008.springplayer.channel.enums.ChannelType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ChannelQueryDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class ChannelQueryDTO extends QueryDTO {

	private ChannelType channelType;

}
