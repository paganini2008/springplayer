package com.github.paganini2008.springplayer.sysinfo.webflux;

import static reactor.netty.Metrics.ACTIVE_CONNECTIONS;
import static reactor.netty.Metrics.CONNECTION_PROVIDER_PREFIX;
import static reactor.netty.Metrics.IDLE_CONNECTIONS;
import static reactor.netty.Metrics.PENDING_CONNECTIONS;
import static reactor.netty.Metrics.TOTAL_CONNECTIONS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.springplayer.common.ApiResult;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Mono;

/**
 * 
 * SysInfoEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnWebApplication(type = Type.REACTIVE)
@RequestMapping("/sys")
@RestController
public class SysInfoEndpoint {

	@PostMapping("/info")
	public Mono<ApiResult<ServerInfo>> getServerInfo() throws Exception {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.refresh();
		return Mono.just(ApiResult.ok(serverInfo));
	}

	@PostMapping("/httppool/info")
	public Mono<ApiResult<Map<String, Number>>> getHttpPoolInfo() {
		final String[] meterNames = { CONNECTION_PROVIDER_PREFIX + TOTAL_CONNECTIONS, CONNECTION_PROVIDER_PREFIX + IDLE_CONNECTIONS,
				CONNECTION_PROVIDER_PREFIX + ACTIVE_CONNECTIONS, CONNECTION_PROVIDER_PREFIX + PENDING_CONNECTIONS };
		MeterRegistry meterRegistry = io.micrometer.core.instrument.Metrics.globalRegistry;
		List<Meter> meters = meterRegistry.getMeters();
		Map<String, Number> meterValues = meters.stream().filter(m -> ArrayUtils.contains(meterNames, m.getId().getName()))
				.collect(Collectors.toMap(m -> m.getId().getName(), m -> meterRegistry.get(m.getId().getName()).gauge().value(),
						(a, b) -> a, HashMap::new));
		return Mono.just(ApiResult.ok(meterValues));
	}

}
