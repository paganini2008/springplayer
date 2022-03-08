package com.github.paganini2008.springplayer.common.ws;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.paganini2008.devtools.collection.MapUtils;

/**
 * 
 * WsConnectionPool
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class WsConnectionPool {

	private final Map<URI, WsConnection> pool = new ConcurrentHashMap<>();

	public WsConnection get(URI uri) {
		return MapUtils.get(pool, uri, () -> {
			WsConnection connection = new WsConnectionImpl(uri);
			connection.connect();
			return connection;
		});
	}

	public void remove(URI uri) {
		pool.remove(uri);
	}

	public int size() {
		return pool.size();
	}

}
