package com.github.paganini2008.springplayer.channel.service;

import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.channel.mapper.AttributeMapper;
import com.github.paganini2008.springplayer.channel.model.Attribute;
import com.github.paganini2008.springplayer.mybatis.PageableIServiceImpl;

/**
 * 
 * AttributeServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class AttributeServiceImpl extends PageableIServiceImpl<AttributeMapper, Attribute> implements AttributeService{

}
