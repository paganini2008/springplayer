package com.github.paganini2008.springplayer.sentinel.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.paganini2008.springplayer.sentinel.RuleType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * SentinelRule
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("sys_sentinel_rule")
@Getter
@Setter
public class SentinelRule {

	@TableId
	private Long id;
	
	@TableField("rule_key")
	private String ruleKey;
	
	@TableField("rule_type")
	private RuleType ruleType;
	
	@TableField("rule")
	private String rule;
	
	@TableField("env")
	private String env;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;
	
	public String getIdentifier() {
		return id != null ? String.valueOf(id) : "";
	}

}
