package com.github.paganini2008.springplayer.common.ws;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.github.paganini2008.devtools.multithreads.Executable;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * WsConnectionImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class WsConnectionImpl extends WebSocketClient implements WsConnection, Executable {

	public WsConnectionImpl(URI serverUri) {
		super(serverUri);
	}

	private final List<String> failedQueue = new CopyOnWriteArrayList<>();
	private Timer timer;

	@Override
	public void connect() {
		super.connect();
	}

	@Override
	public void send(String content) {
		if (isActive()) {
			super.send(content);
		} else {
			failedQueue.add(content);
		}
	}

	@Override
	public void disconnect() {
		super.close();
	}

	@Override
	public boolean isActive() {
		return super.isOpen();
	}

	@Override
	public void close() {
		super.close();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public synchronized void onOpen(ServerHandshake handshakedata) {
		if (log.isInfoEnabled()) {
			log.info("[OnOpen] status: {}, message: {}", handshakedata.getHttpStatus(), handshakedata.getHttpStatusMessage());
		}
		if (timer == null) {
			timer = ThreadUtils.scheduleWithFixedDelay(this, 1, TimeUnit.MINUTES);
		}
		if (failedQueue.size() > 0) {
			List<String> copy = new ArrayList<>(failedQueue);
			failedQueue.removeAll(copy);
			for (String content : copy) {
				send(content);
			}
		}
	}

	@Override
	public void onMessage(String message) {
		if (log.isInfoEnabled()) {
			log.info("[OnMessage] content: {}", message);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		if (log.isInfoEnabled()) {
			log.info("[OnClose] code: {}, reason: {}, remote: {}", code, reason, remote);
		}
	}

	@Override
	public void onError(Exception e) {
		if (log.isErrorEnabled()) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean execute() throws Throwable {
		if (isClosed()) {
			ThreadUtils.sleep(1000L);
			connect();
		}
		return true;
	}

}
