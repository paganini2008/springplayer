package com.github.paganini2008.springplayer.upms.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.upms.model.RolePermission;

/**
 * 
 * RolePermissionService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface RolePermissionService extends IService<RolePermission> {

	List<RolePermission> getByEmployeeId(Long empId);
}
