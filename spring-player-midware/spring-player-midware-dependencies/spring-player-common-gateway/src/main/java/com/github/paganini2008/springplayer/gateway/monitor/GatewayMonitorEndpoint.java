package com.github.paganini2008.springplayer.gateway.monitor;

import static com.github.paganini2008.springplayer.gateway.GatewayConstants.GENERIC_DATETIME_PATTERNS;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.time.DateUtils;
import com.github.paganini2008.springplayer.common.ApiResult;

import reactor.core.publisher.Mono;

/**
 * 
 * 监控相关界面接口
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@CrossOrigin(origins = "*")
@Validated
@RestController
@RequestMapping("/admin/yl-gateway-admin/monitor")
public class GatewayMonitorEndpoint {

	@Autowired(required = false)
	private ApiWatcherContext context;

	/**
	 * 监控总览
	 * 
	 * @param pathPattern
	 * @return
	 */
	@PostMapping("/summary")
	public Mono<ApiResult<Map<String, Object>>> getSummary(@RequestParam(name = "path", required = false) String pathPattern) {
		checkMonitorEnabled();
		if (StringUtils.isBlank(pathPattern)) {
			pathPattern = "/**";
		}
		Map<String, Object> desc = context.summary(pathPattern);
		return Mono.just(ApiResult.ok(desc));
	}

	/**
	 * 获取监控元数据
	 * 
	 * @return
	 */
	@PostMapping("/metadata")
	public Mono<ApiResult<Map<String, Map<String, Object>>>> getMetadata(@RequestParam(name = "path", required = false) String pathPattern,
			@RequestParam(name = "startTime", required = false) String startTime) {
		checkMonitorEnabled();
		if (StringUtils.isBlank(pathPattern)) {
			pathPattern = "/**";
		}
		Date startDate = null;
		if (StringUtils.isNotBlank(startTime)) {
			startDate = DateUtils.parse(startTime, GENERIC_DATETIME_PATTERNS);
		}
		Map<String, Map<String, Object>> desc = context.sequence(pathPattern, startDate);
		return Mono.just(ApiResult.ok(desc));
	}

	@GetMapping("/traces")
	public Mono<ApiResult<Collection<String>>> getTraces() {
		checkMonitorEnabled();
		Collection<String> paths = context.paths();
		return Mono.just(ApiResult.ok(paths));
	}

	@PostMapping("/trace/add")
	public Mono<ApiResult<String>> addTrace(@Validated @RequestBody TraceDTO traceDTO) {
		context.watchPath(traceDTO.getPathPattern(), traceDTO.getLabels());
		return Mono.just(ApiResult.ok("新增成功"));
	}

	@PostMapping("/trace/delete")
	public Mono<ApiResult<String>> deleteTrace(@Validated @RequestBody TraceDTO traceDTO) {
		context.unwatchPath(traceDTO.getPathPattern());
		return Mono.just(ApiResult.ok("删除成功"));
	}

	@GetMapping("/latest/rt")
	public Mono<ApiResult<Map<String, Object>>> getLatestRt() {
		checkMonitorEnabled();
		return Mono.just(ApiResult.ok(context.getLatestRt()));
	}

	@GetMapping("/latest/cc")
	public Mono<ApiResult<Map<String, Object>>> getLatestCc() {
		checkMonitorEnabled();
		return Mono.just(ApiResult.ok(context.getLatestCc()));
	}

	@GetMapping("/latest/status")
	public Mono<ApiResult<Map<String, Object>>> getLatestHttpStatus() {
		checkMonitorEnabled();
		return Mono.just(ApiResult.ok(context.getLatestHttpStatus()));
	}

	@GetMapping("/latest/qps")
	public Mono<ApiResult<Map<String, Object>>> getLatestQps() {
		checkMonitorEnabled();
		return Mono.just(ApiResult.ok(context.getLatestQps()));
	}

	@GetMapping("/latest/traces")
	public Mono<ApiResult<List<HttpTrace>>> getLatestHttpTraces(@RequestParam("path") String path) {
		checkMonitorEnabled();
		return Mono.just(ApiResult.ok(context.getLatestHttpTraces(path)));
	}

	@GetMapping("/traces/search")
	public Mono<ApiResult<Map<String, Map<String, List<HttpTrace>>>>> searchTraces(@RequestParam("path") String path,
			@RequestParam(name = "startDate", required = false) String startDateStr,
			@RequestParam(name = "endDate", required = false) String endDateStr) {
		checkMonitorEnabled();
		Date startDate = null, endDate = null;
		if (StringUtils.isNotBlank(startDateStr)) {
			startDate = DateUtils.parse(startDateStr, GENERIC_DATETIME_PATTERNS);
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			endDate = DateUtils.parse(endDateStr, GENERIC_DATETIME_PATTERNS);
		}
		return Mono.just(ApiResult.ok(context.searchTraces(path, startDate, endDate)));
	}

	private void checkMonitorEnabled() {
		if (context == null) {
			throw new UnsupportedOperationException("请先开启监控功能");
		}
	}

}
