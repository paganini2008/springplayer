package com.github.paganini2008.springplayer.upms.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.upms.model.RoleEmployee;

/**
 * 
 * RoleEmployeeService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface RoleEmployeeService extends IService<RoleEmployee> {

	List<RoleEmployee> getByEmployeeId(Long empId);

}
