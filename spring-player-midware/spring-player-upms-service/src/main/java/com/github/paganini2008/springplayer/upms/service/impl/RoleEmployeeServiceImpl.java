package com.github.paganini2008.springplayer.upms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.upms.mapper.RoleEmployeeMapper;
import com.github.paganini2008.springplayer.upms.model.RoleEmployee;
import com.github.paganini2008.springplayer.upms.service.RoleEmployeeService;

/**
 * 
 * RoleEmployeeServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class RoleEmployeeServiceImpl extends ServiceImpl<RoleEmployeeMapper, RoleEmployee>
		implements RoleEmployeeService {

	@Override
	public List<RoleEmployee> getByEmployeeId(Long empId) {
		LambdaQueryWrapper<RoleEmployee> query = Wrappers.<RoleEmployee>lambdaQuery().eq(RoleEmployee::getEmployeeId,
				empId);
		return list(query);
	}

}
