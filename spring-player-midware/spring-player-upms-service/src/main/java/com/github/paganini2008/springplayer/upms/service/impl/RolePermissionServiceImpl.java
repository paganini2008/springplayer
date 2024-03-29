package com.github.paganini2008.springplayer.upms.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.upms.mapper.RolePermissionMapper;
import com.github.paganini2008.springplayer.upms.model.RolePermission;
import com.github.paganini2008.springplayer.upms.service.RolePermissionService;

/**
 * 
 * RolePermissionServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

	@Override
	public List<RolePermission> getByRoleIds(Long... roleIds) {
		LambdaQueryWrapper<RolePermission> query = Wrappers.<RolePermission>lambdaQuery().in(RolePermission::getRoleId,
				Arrays.asList(roleIds));
		return list(query);
	}

}
