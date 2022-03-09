package com.github.paganini2008.springplayer.common.id;

/**
 * 
 * Uuid1IdGeneratorFactory
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class Uuid1IdGeneratorFactory implements IdGeneratorFactory {

	@Override
	public IdGenerator getObject() throws Exception {
		return new Uuid1IdGenerator();
	}

	@Override
	public Class<?> getObjectType() {
		return Uuid1IdGenerator.class;
	}

}
