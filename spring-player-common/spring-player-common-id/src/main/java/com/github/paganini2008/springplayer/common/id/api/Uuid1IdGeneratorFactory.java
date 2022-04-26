package com.github.paganini2008.springplayer.common.id.api;

import com.github.paganini2008.springplayer.common.id.IdGenerator;
import com.github.paganini2008.springplayer.common.id.IdGeneratorFactory;

/**
 * 
 * Uuid1IdGeneratorFactory
 *
 * @author Feng Yan
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
