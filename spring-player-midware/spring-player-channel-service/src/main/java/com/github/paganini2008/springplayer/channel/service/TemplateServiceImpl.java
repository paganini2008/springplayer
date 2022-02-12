package com.github.paganini2008.springplayer.channel.service;

import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.channel.mapper.TemplateMapper;
import com.github.paganini2008.springplayer.channel.model.Template;
import com.github.paganini2008.springplayer.mybatis.PageableIServiceImpl;

/**
 * 
 * TemplateServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class TemplateServiceImpl extends PageableIServiceImpl<TemplateMapper, Template> implements TemplateService {

}
