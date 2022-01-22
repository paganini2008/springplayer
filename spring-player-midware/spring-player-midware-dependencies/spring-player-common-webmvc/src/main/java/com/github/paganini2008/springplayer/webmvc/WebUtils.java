package com.github.paganini2008.springplayer.webmvc;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * WebUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public abstract class WebUtils {

	public static String getCurrentIpAddress() {
		return getIpAddress(getRequest());
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static void main(String[] args) {
		System.out.println(getHostUrl("https://d-linking.tech/login.html"));
	}

	public static String getHostUrl(String url) {
		String hostUrl = "";
		try {
			URL u = new URL(url);
			hostUrl = u.getProtocol() + "://" + u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return hostUrl;
	}

	public static String getContextPath(HttpServletRequest request) {
		return getHostUrl(request.getRequestURL().toString()) + ":" + request.getServerPort() + request.getContextPath();
	}

	public static HttpServletRequest getRequiredRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	public static HttpServletRequest getRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (RuntimeException e) {
			return null;
		}
	}

	public static Cookie getCookie(String name) {
		HttpServletRequest request = getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static <T> T getSessionAttr(String attrName) {
		return (T) getSession().getAttribute(attrName);
	}

	public static ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	public static String getWebRoot(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path.replace("\\", "/");
		return path;
	}

}
