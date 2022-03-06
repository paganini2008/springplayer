package com.github.paganini2008.springplayer.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 
 * SnowFlakeIdGenerator
 *
 * @author Fred Feng
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
