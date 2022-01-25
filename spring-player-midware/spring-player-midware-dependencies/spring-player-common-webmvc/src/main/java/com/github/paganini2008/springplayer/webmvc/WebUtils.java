package com.github.paganini2008.springplayer.webmvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.paganini2008.devtools.StringUtils;

import feign.Response;

/**
 * 
 * WebUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public abstract class WebUtils {

	public static HttpHeaders copyHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		Collections.list(request.getHeaderNames()).forEach(headerName -> {
			Enumeration<String> en = request.getHeaders(headerName);
			headers.addAll(headerName, en != null ? Collections.list(en) : new ArrayList<>());
		});
		return headers;
	}

	public static HttpHeaders copyHeaders(HttpServletResponse response) {
		HttpHeaders headers = new HttpHeaders();
		response.getHeaderNames().forEach(headerName -> {
			Collection<String> c = response.getHeaders(headerName);
			headers.addAll(headerName, c != null ? new ArrayList<>(c) : new ArrayList<>());
		});
		return headers;
	}

	public static HttpHeaders copyHeaders(Response response) {
		HttpHeaders headers = new HttpHeaders();
		response.headers().entrySet().forEach(e -> {
			headers.addAll(e.getKey(), e.getValue() != null ? new ArrayList<>(e.getValue()) : new ArrayList<>());
		});
		return headers;
	}

	public static String getLang(HttpServletRequest request) {
		return getLang(request, Locale.getDefault().getLanguage());
	}

	public static String getLang(HttpServletRequest request, String defaultLang) {
		String lang = request.getHeader("lang");
		if (StringUtils.isBlank(lang)) {
			lang = request.getHeader("Lang");
		}
		return lang;
	}

	public static String getIpAddr() {
		return getIpAddr(getRequest());
	}

	public static String getIpAddr(HttpServletRequest request) {
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
