package com.github.paganini2008.springplayer.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.upms.model.Employee;

/**
 * 
 * EmployeeService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface EmployeeService extends IService<Employee> {

	Employee findEmployeeByName(String username);

}
