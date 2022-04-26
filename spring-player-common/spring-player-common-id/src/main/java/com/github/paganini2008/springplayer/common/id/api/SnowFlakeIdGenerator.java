package com.github.paganini2008.springplayer.common.id.api;

import com.github.paganini2008.springplayer.common.id.IdGenerator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 
 * SnowFlakeIdGenerator
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public class SnowFlakeIdGenerator implements IdGenerator {

	public SnowFlakeIdGenerator(long workerId, long datacenterId) {
		this.snowflake = IdUtil.createSnowflake(workerId, datacenterId);
	}

	private Snowflake snowflake;
	private volatile long latestId;

	@Override
	public long currentId() {
		return latestId;
	}

	@Override
	public long generateId() {
		return (latestId = snowflake.nextId());
	}

}
