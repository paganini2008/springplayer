package com.github.paganini2008.springplayer.upms.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Enterprise
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@TableName("sys_enterprise")
public class Enterprise {

	@TableId
	private Long id;

	@TableField("name")
	private String name;

	@TableField("country")
	private String country;

	@TableField("address")
	private String address;

	@TableField("postcode")
	private String postcode;

	@TableField("contact")
	private String contact;

	@TableField("phone")
	private String phone;

	@TableField("email")
	private String email;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
