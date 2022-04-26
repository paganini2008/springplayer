package com.github.paganini2008.springplayer.common.id;

import lombok.RequiredArgsConstructor;

/**
 * 
 * RemoteIdGenerator
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
@RequiredArgsConstructor
public class RemoteIdGenerator implements IdGenerator {

	private final RemoteIdService remoteIdService;

	@Override
	public long currentId() {
		return remoteIdService.getCurrentValue().getData();
	}

	@Override
	public long generateId() {
		return remoteIdService.getNextValue().getData();
	}

}
