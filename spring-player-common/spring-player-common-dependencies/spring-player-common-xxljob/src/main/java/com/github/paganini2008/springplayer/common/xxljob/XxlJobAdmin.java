package com.github.paganini2008.springplayer.common.xxljob;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.xxl.job.admin.core.model.XxlJobInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * XxlJobAdmin
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class XxlJobAdmin implements InitializingBean, DisposableBean {

	public XxlJobAdmin(XxlJobProperties config) {
		this(config, new RestTemplate());
	}

	public XxlJobAdmin(XxlJobProperties config, RestTemplate restTemplate) {
		this.config = config;
		this.restTemplate = restTemplate;
	}

	private final XxlJobProperties config;
	private final RestTemplate restTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		login();
	}

	private String cookieString;

	public void login() {
		String path = config.getUrl() + "/login?userName=" + config.getUsername() + "&password=" + config.getPassword();
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(path, null, String.class);
		if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
			return;
		}
		List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
		if (CollectionUtils.isNotEmpty(cookies)) {
			List<String> cookies2 = responseEntity.getHeaders().get("Set-Cookie2");
			if (CollectionUtils.isNotEmpty(cookies2)) {
				cookies.addAll(cookies2);
			}
			cookieString = CollectionUtils.join(cookies, ";");
			log.info("Login! Cookie: {}", cookieString);
		}
	}

	public void logout() {
		String path = config.getUrl() + "/logout";
		ResponseEntity<String> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, String.class);
		if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
			log.info("Logout!");
		}
	}

	public Integer addJob(XxlJobInfo jobInfo) {
		String path = config.getUrl() + "/jobinfo/add";
		HttpEntity<XxlJobInfo> httpEntity = new HttpEntity<XxlJobInfo>(jobInfo);
		httpEntity.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		httpEntity.getHeaders().set("Cookie", cookieString);
		ResponseEntity<String> responseEntity = restTemplate.exchange(path, HttpMethod.POST, httpEntity, String.class);
		if (responseEntity == null || StringUtils.isBlank(responseEntity.getBody())) {
			return null;
		}
		Integer id = null;
		try {
			id = Integer.valueOf(responseEntity.getBody());
			log.info("Add JobInfo: {}", id);
		} catch (RuntimeException e) {
		}
		return id;
	}

	public void updateJob(XxlJobInfo jobInfo) {
		String path = config.getUrl() + "/jobinfo/update";
		HttpEntity<XxlJobInfo> httpEntity = new HttpEntity<XxlJobInfo>(jobInfo);
		httpEntity.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		httpEntity.getHeaders().set("Cookie", cookieString);
		ResponseEntity<String> responseEntity = restTemplate.exchange(path, HttpMethod.POST, httpEntity, String.class);
		if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
			log.info("Update JobInfo: {}", jobInfo.getId());
		}
	}

	public void removeJob(int id) {
		String path = config.getUrl() + "/jobinfo/remove?id=" + id;
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(path, null, String.class);
		if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
			log.info("Remove JobInfo: {}", id);
		}
	}

	public void startJob(int id) {
		String path = config.getUrl() + "/jobinfo/start?id=" + id;
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(path, null, String.class);
		if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
			log.info("Start JobInfo: {}", id);
		}
	}

	public void stopJob(int id) {
		String path = config.getUrl() + "/jobinfo/stop?id=" + id;
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(path, null, String.class);
		if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
			log.info("Stop JobInfo: {}", id);
		}
	}

	@Override
	public void destroy() throws Exception {
		logout();
	}

}
