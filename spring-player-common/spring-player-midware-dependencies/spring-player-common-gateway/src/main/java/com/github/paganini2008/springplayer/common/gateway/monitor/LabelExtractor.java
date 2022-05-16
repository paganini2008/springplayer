package com.github.paganini2008.springplayer.common.gateway.monitor;

/**
 * 
 * LabelExtractor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface LabelExtractor {

	Object extractLabel(String labelName, HttpTrace httpTrace);

}
