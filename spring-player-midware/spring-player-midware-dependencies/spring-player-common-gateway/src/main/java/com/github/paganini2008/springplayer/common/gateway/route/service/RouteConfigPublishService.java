package com.github.paganini2008.springplayer.common.gateway.route.service;

import static com.github.paganini2008.springplayer.common.gateway.GatewayConstants.REDIS_KEY_ROUTE;
import static com.github.paganini2008.springplayer.common.gateway.GatewayConstants.REDIS_PUBSUB_CHANNEL_ROUTE_PUBLISH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.springplayer.common.gateway.route.model.RouteConfig;
import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSubService;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RouteConfigPublishService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Service
public class RouteConfigPublishService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RedisPubSubService redisPubSubService;

	@Autowired(required = false)
	private TaskScheduler taskScheduler;

	@Autowired
	private RouteConfigService routeConfigService;

	@Value("${spring.profiles.active:default}")
	private String env;

	/**
	 * 发布全部的路由配置
	 * 
	 * @param refresh 是否通知集群强制刷新
	 */
	public void publishAll(boolean forceRefresh) {
		LambdaQueryWrapper<RouteConfig> query = Wrappers.<RouteConfig>lambdaQuery().eq(RouteConfig::getEnv, env);
		List<RouteConfig> configs = routeConfigService.list(query);
		boolean updated = false;
		List<String> changedServiceIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(configs)) {
			for (RouteConfig config : configs) {
				updated |= doPublish(config);
				changedServiceIds.add(config.getServiceId());
			}
		}
		if (updated && forceRefresh) {
			forceRefresh(0, changedServiceIds.toArray(new String[0]));
		}
	}

	/**
	 * 根据组名发布路由配置
	 * 
	 * @param groupName    组名
	 * @param forceRefresh 是否通知集群强制刷新
	 */
	public void publishGroup(String groupName, boolean forceRefresh) {
		LambdaQueryWrapper<RouteConfig> query = Wrappers.<RouteConfig>lambdaQuery().eq(RouteConfig::getEnv, env)
				.eq(RouteConfig::getGroupName, groupName);
		List<RouteConfig> configs = routeConfigService.list(query);
		boolean updated = false;
		List<String> changedServiceIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(configs)) {
			for (RouteConfig config : configs) {
				updated |= doPublish(config);
				changedServiceIds.add(config.getServiceId());
			}
		}
		if (updated && forceRefresh) {
			forceRefresh(0, changedServiceIds.toArray(new String[0]));
		}
	}

	/**
	 * 根据服务名发布路由配置
	 * 
	 * @param serviceId    服务名
	 * @param forceRefresh 是否通知集群强制刷新
	 */
	public void publishServiceId(String serviceId, boolean forceRefresh) {
		LambdaQueryWrapper<RouteConfig> query = Wrappers.<RouteConfig>lambdaQuery().eq(RouteConfig::getEnv, env)
				.eq(RouteConfig::getServiceId, serviceId);
		RouteConfig config = routeConfigService.getOne(query);
		if (config != null) {
			boolean updated = doPublish(config);
			if (updated && forceRefresh) {
				forceRefresh(0, new String[] { serviceId });
			}
		}

	}

	private boolean doPublish(RouteConfig rc) {
		String serviceId = rc.getServiceId();
		RouteConfigDTO previous = null;
		try {
			previous = (RouteConfigDTO) redisTemplate.opsForHash().get(REDIS_KEY_ROUTE, serviceId);
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			previous = null;
		}
		RouteConfigDTO latest = JacksonUtils.parseJson(rc.getRule(), RouteConfigDTO.class);
		if (previous == null || !latest.equals(previous)) {
			redisTemplate.opsForHash().delete(REDIS_KEY_ROUTE, serviceId);
			redisTemplate.opsForHash().put(REDIS_KEY_ROUTE, serviceId, latest);
			log.info("路由'{}'发现更新内容", serviceId);
			return true;
		}
		return false;
	}

	/**
	 * 发送Redis消息通知Gateway集群强制刷新路由配置
	 * 
	 * @param delay
	 */
	private void forceRefresh(int delay, String[] serviceIds) {
		if (delay > 0 && taskScheduler != null) {
			Date future = DateUtils.addSeconds(new Date(), delay);
			taskScheduler.schedule(() -> {
				redisPubSubService.convertAndMulticast(REDIS_PUBSUB_CHANNEL_ROUTE_PUBLISH, Arrays.toString(serviceIds));
			}, future);
		} else if (delay == 0) {
			redisPubSubService.convertAndMulticast(REDIS_PUBSUB_CHANNEL_ROUTE_PUBLISH, Arrays.toString(serviceIds));
		}
	}

	@Async
	@EventListener({ ContextRefreshedEvent.class })
	public void initializeRoute() {
		try {
			publishAll(false);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		log.info("初始化路由结束");
	}

}
