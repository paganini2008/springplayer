package com.github.paganini2008.springplayer.channel.service;

import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.channel.mapper.ChannelMapper;
import com.github.paganini2008.springplayer.channel.model.Channel;
import com.github.paganini2008.springplayer.common.mybatis.PageableIServiceImpl;

/**
 * 
 * ChannelServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class ChannelServiceImpl extends PageableIServiceImpl<ChannelMapper, Channel> implements ChannelService {

}
