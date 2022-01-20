package com.github.paganini2008.springplayer.upms.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.upms.mapper.EnterpriseMapper;
import com.github.paganini2008.springplayer.upms.model.Enterprise;
import com.github.paganini2008.springplayer.upms.service.EnterpriseService;

/**
 * 
 * EnterpriseServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {
}
