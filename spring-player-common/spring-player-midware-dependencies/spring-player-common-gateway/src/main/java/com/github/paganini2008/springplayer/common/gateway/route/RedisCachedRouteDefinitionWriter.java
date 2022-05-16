package com.github.paganini2008.springplayer.common.gateway.route;

import static com.github.paganini2008.springplayer.common.gateway.GatewayConstants.REDIS_KEY_ROUTE;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * RedisCachedRouteDefinitionWriter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class RedisCachedRouteDefinitionWriter implements RouteDefinitionRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		List<Object> dataList;
		try {
			dataList = redisTemplate.opsForHash().values(REDIS_KEY_ROUTE);
		} catch (RuntimeException e) {
			log.warn("加载路由配置信息错误：" + e.getMessage());
			dataList = new ArrayList<>();
		}
		if (CollectionUtils.isEmpty(dataList)) {
			return Flux.empty();
		}
		return Flux.fromIterable(dataList.stream().map(o -> {
			RouteConfigDTO configDTO = (RouteConfigDTO) o;
			RouteDefinition rd = new RouteDefinition();
			rd.setId(configDTO.getId());
			rd.setUri(URI.create(configDTO.getUri()));
			rd.setMetadata(configDTO.getMetadata());
			rd.setOrder(configDTO.getOrder());

			if (!CollectionUtils.isEmpty(configDTO.getPredicates())) {
				rd.setPredicates(configDTO.getPredicates().stream().map(n -> {
					if (StringUtils.isNotBlank(n.getText())) {
						return new PredicateDefinition(n.getText());
					}
					PredicateDefinition pd = new PredicateDefinition();
					pd.setName(n.getName());
					pd.setArgs(n.getArgs());
					return pd;
				}).collect(Collectors.toList()));
			}

			if (!CollectionUtils.isEmpty(configDTO.getFilters())) {
				rd.setFilters(configDTO.getFilters().stream().map(n -> {
					if (StringUtils.isNotBlank(n.getText())) {
						return new FilterDefinition(n.getText());
					}
					FilterDefinition fd = new FilterDefinition();
					fd.setName(n.getName());
					fd.setArgs(n.getArgs());
					return fd;
				}).collect(Collectors.toList()));
			}
			if (log.isTraceEnabled()) {
				log.trace("加载路由信息：{}", rd);
			}
			return rd;
		}).collect(Collectors.toList()));
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			RouteConfigDTO configDTO = new RouteConfigDTO();
			configDTO.setId(r.getId());
			if (r.getUri() != null) {
				configDTO.setUri(r.getUri().toString());
			}
			configDTO.setMetadata(r.getMetadata());
			configDTO.setOrder(r.getOrder());
			if (!CollectionUtils.isEmpty(r.getFilters())) {
				List<RouteConfigDTO.NameArgs> list = r.getFilters().stream()
						.map(fd -> new RouteConfigDTO.NameArgs(fd.getName(), fd.getArgs())).collect(Collectors.toList());
				configDTO.setFilters(list);
			}
			if (!CollectionUtils.isEmpty(r.getPredicates())) {
				List<RouteConfigDTO.NameArgs> list = r.getPredicates().stream()
						.map(pd -> new RouteConfigDTO.NameArgs(pd.getName(), pd.getArgs())).collect(Collectors.toList());
				configDTO.setPredicates(list);
			}
			redisTemplate.opsForHash().put(REDIS_KEY_ROUTE, r.getId(), configDTO);
			log.info("添加临时新路由: {}", configDTO);
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(id -> {
			redisTemplate.opsForHash().delete(REDIS_KEY_ROUTE, id);
			log.info("从缓存中删除路由: {}", id);
		});
		return Mono.empty();
	}

}
