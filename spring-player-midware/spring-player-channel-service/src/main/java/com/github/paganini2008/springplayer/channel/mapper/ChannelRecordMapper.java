package com.github.paganini2008.springplayer.channel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.github.paganini2008.springplayer.channel.model.ChannelRecord;
import com.github.paganini2008.springplayer.common.mybatis.EntityMapper;

/**
 * 
 * ChannelRecordMapper
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Mapper
public interface ChannelRecordMapper extends EntityMapper<ChannelRecord> {
}
