package com.github.paganini2008.springplayer.common.id.api;

import com.github.paganini2008.springplayer.common.id.IdGenerator;

/**
 * 
 * Uuid1IdGenerator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class Uuid1IdGenerator implements IdGenerator {

	private volatile long latestId;

	@Override
	public long currentId() {
		return latestId;
	}

	@Override
	public long generateId() {
		return latestId = Uuid1Utils.getTimeBasedUuid().timestamp();
	}

}
