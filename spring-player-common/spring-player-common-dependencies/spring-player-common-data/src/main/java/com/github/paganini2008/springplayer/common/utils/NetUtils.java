package com.github.paganini2008.springplayer.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;

import com.github.paganini2008.devtools.primitives.Bytes;

/**
 * 
 * NetUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class NetUtils {

	public static byte[] getMacAddress() {
		byte[] mac = null;
		try {
			final InetAddress localHost = InetAddress.getLocalHost();
			try {
				final NetworkInterface localInterface = NetworkInterface.getByInetAddress(localHost);
				if (isUpAndNotLoopback(localInterface)) {
					mac = localInterface.getHardwareAddress();
				}
				if (mac == null) {
					final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
					if (networkInterfaces != null) {
						while (networkInterfaces.hasMoreElements() && mac == null) {
							final NetworkInterface nic = networkInterfaces.nextElement();
							if (isUpAndNotLoopback(nic)) {
								mac = nic.getHardwareAddress();
							}
						}
					}
				}
			} catch (final SocketException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
			if (Bytes.isEmpty(mac) && localHost != null) {
				// Emulate a MAC address with an IP v4 or v6
				final byte[] address = localHost.getAddress();
				// Take only 6 bytes if the address is an IPv6 otherwise will pad with two zero
				// bytes
				mac = Arrays.copyOf(address, 6);
			}
		} catch (final UnknownHostException ignored) {
			// ignored
		}
		return mac;
	}
	

    private static boolean isUpAndNotLoopback(final NetworkInterface ni) throws SocketException {
        return ni != null && !ni.isLoopback() && ni.isUp();
    }

}
