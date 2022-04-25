package com.github.paganini2008.springplayer.gateway.monitor;

/**
 * 
 * LabelExtractor
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public interface LabelExtractor {

	Object extractLabel(String labelName, HttpTrace httpTrace);

}
