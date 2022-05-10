package com.github.paganini2008.springplayer.upms.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.springplayer.common.upms.vo.DeptVO;
import com.github.paganini2008.springplayer.common.upms.vo.EnterpriseVO;
import com.github.paganini2008.springplayer.common.upms.vo.PermissionVO;
import com.github.paganini2008.springplayer.common.upms.vo.RoleVO;
import com.github.paganini2008.springplayer.common.upms.vo.UserVO;
import com.github.paganini2008.springplayer.upms.ErrorCodes;
import com.github.paganini2008.springplayer.upms.UpmsException;
import com.github.paganini2008.springplayer.upms.model.Dept;
import com.github.paganini2008.springplayer.upms.model.Employee;
import com.github.paganini2008.springplayer.upms.model.Enterprise;
import com.github.paganini2008.springplayer.upms.model.Permission;
import com.github.paganini2008.springplayer.upms.model.Role;
import com.github.paganini2008.springplayer.upms.model.RoleEmployee;
import com.github.paganini2008.springplayer.upms.model.RolePermission;
import com.github.paganini2008.springplayer.upms.service.DeptService;
import com.github.paganini2008.springplayer.upms.service.EmployeeService;
import com.github.paganini2008.springplayer.upms.service.EnterpriseService;
import com.github.paganini2008.springplayer.upms.service.PermissionService;
import com.github.paganini2008.springplayer.upms.service.RoleEmployeeService;
import com.github.paganini2008.springplayer.upms.service.RolePermissionService;
import com.github.paganini2008.springplayer.upms.service.RoleService;

/**
 * 
 * UserManagerService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class UserManagerService {

	@Autowired
	private EnterpriseService enterpriseService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RolePermissionService rolePermissionService;

	@Autowired
	private RoleEmployeeService roleEmployeeService;

	@Autowired
	public PermissionService permissionService;

	public UserVO getUserInfo(String username) {
		Employee employee = employeeService.findEmployeeByName(username);
		if (employee == null) {
			throw new UpmsException(ErrorCodes.EMPLOYEE_NOT_FOUND);
		}
		return getUserInfo(employee.getId());
	}

	public UserVO getUserInfo(Long id) {
		UserVO user = new UserVO();
		Employee employee = employeeService.getById(id);
		user.setId(employee.getId());
		user.setFullname(employee.getFullname());
		user.setUsername(employee.getUsername());
		user.setPassword(employee.getPassword());
		user.setEnabled(employee.getEnabled().intValue() == 1);

		Enterprise enterprise = enterpriseService.getById(employee.getEnterpriseId());
		EnterpriseVO enterpriseVO = new EnterpriseVO();
		enterpriseVO.setId(enterprise.getId());
		enterpriseVO.setName(enterprise.getName());
		user.setEnterprise(enterpriseVO);

		Dept dept = deptService.getById(employee.getDeptId());
		DeptVO deptVO = new DeptVO();
		deptVO.setId(dept.getId());
		deptVO.setName(dept.getName());
		deptVO.setLevel(dept.getLevel());
		user.setDept(deptVO);

		List<RoleEmployee> roleEmployees = roleEmployeeService.getByEmployeeId(employee.getId());
		if (CollectionUtils.isNotEmpty(roleEmployees)) {
			List<Long> roleIds = roleEmployees.stream().map(re -> re.getRoleId()).collect(Collectors.toList());
			user.setRoles(roleIds.stream().map(rid -> {
				Role role = roleService.getById(rid);
				RoleVO roleVO = new RoleVO();
				roleVO.setId(role.getId());
				roleVO.setName(role.getName());
				roleVO.setCode(role.getCode());
				return roleVO;
			}).toArray(l -> new RoleVO[l]));
		}
		Long[] roleIds = Arrays.stream(user.getRoles()).map(r -> r.getId()).toArray(l -> new Long[l]);
		List<RolePermission> rolePermissions = rolePermissionService.getByRoleIds(roleIds);
		if (CollectionUtils.isNotEmpty(rolePermissions)) {
			List<Long> permIds = rolePermissions.stream().map(rp -> rp.getPermissionId()).collect(Collectors.toList());
			user.setPermissions(permIds.stream().map(pid -> {
				Permission permission = permissionService.getById(pid);
				PermissionVO permissionVO = new PermissionVO();
				permissionVO.setId(permission.getId());
				permissionVO.setName(permission.getName());
				permissionVO.setPath(permission.getPath());
				permissionVO.setCode(permission.getCode());
				return permissionVO;
			}).toArray(l -> new PermissionVO[l]));
		}
		return user;
	}

}
