package com.github.paganini2008.springplayer.upms.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.upms.mapper.EmployeeMapper;
import com.github.paganini2008.springplayer.upms.model.Employee;
import com.github.paganini2008.springplayer.upms.service.EmployeeService;

/**
 * 
 * EmployeeServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

	public Employee findEmployeeByName(String username) {
		LambdaQueryWrapper<Employee> query = Wrappers.<Employee>lambdaQuery().eq(Employee::getUsername, username);
		return getOne(query);
	}
	
	
}
