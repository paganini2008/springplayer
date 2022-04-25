package com.github.paganini2008.springplayer.gateway.monitor;

/**
 * 
 * EnhancedPathMatcherMap
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public class EnhancedPathMatcherMap<V> extends PathMatcherMap<V> {

	private static final long serialVersionUID = 8200062343096448994L;

	public EnhancedPathMatcherMap() {
		super();
	}

	@Override
	protected boolean match(String originalStr, Object inputKey) {
		int index = originalStr.indexOf("+");
		String originalPath = originalStr.substring(0, index);
		String originalLabel = originalStr.substring(index + 1);

		String str = (String) inputKey;
		index = str.indexOf("+");
		String path = str.substring(0, index);
		String label = str.substring(index + 1);

		return super.match(originalPath, path) && originalLabel.equals(label);
	}

}
