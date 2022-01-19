package com.github.paganini2008.springplayer.upms.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Dept
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@TableName("sys_dept")
public class Dept {

	@TableId
	private Long id;

	@TableField("name")
	private String name;

	@TableField("pid")
	private Long pid;

	@TableField("level")
	private Integer level;

	@TableField("leader")
	private Long leader;

	@TableField("enterprise_id")
	private Long enterpriseId;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
