package com.github.paganini2008.springplayer.common.id;

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
