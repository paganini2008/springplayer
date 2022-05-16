package com.github.paganini2008.springplayer.common.id;

import lombok.RequiredArgsConstructor;

/**
 * 
 * RemoteIdGenerator
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@RequiredArgsConstructor
public class RemoteIdGenerator implements IdGenerator {

	private final RemoteIdService remoteIdService;

	@Override
	public long currentId() {
		Long data = remoteIdService.getCurrentValue().getData();
		return data != null ? data.longValue() : 0;
	}

	@Override
	public long generateId() {
		Long data = remoteIdService.getNextValue().getData();
		return data != null ? data.longValue() : 0;
	}

}
