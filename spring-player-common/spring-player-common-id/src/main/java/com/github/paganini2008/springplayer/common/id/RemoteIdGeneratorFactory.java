package com.github.paganini2008.springplayer.common.id;

import lombok.RequiredArgsConstructor;

/**
 * 
 * RemoteIdGeneratorFactory
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@RequiredArgsConstructor
public class RemoteIdGeneratorFactory implements IdGeneratorFactory {

	private final RemoteIdService remoteIdService;

	@Override
	public IdGenerator getObject() throws Exception {
		return new RemoteIdGenerator(remoteIdService);
	}

	@Override
	public Class<?> getObjectType() {
		return RemoteIdGenerator.class;
	}

}
