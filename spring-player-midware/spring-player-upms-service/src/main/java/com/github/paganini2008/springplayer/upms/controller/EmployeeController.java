package com.github.paganini2008.springplayer.upms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.upms.model.Employee;
import com.github.paganini2008.springplayer.upms.service.EmployeeService;

/**
 * 
 * EmployeeController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestController
@RequestMapping("/emp")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/find/{username}")
	public ApiResult<Employee> findEmployeeByName(@PathVariable("username") String username) {
		Employee employee = employeeService.findEmployeeByName(username);
		return ApiResult.ok(employee);
	}

}
