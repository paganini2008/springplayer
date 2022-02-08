package com.github.paganini2008.springplayer.gateway.route;

import static com.github.paganini2008.springplayer.gateway.GatewayConstants.REDIS_KEY_ROUTE;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.ApplicationContextUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.gateway.route.model.RouteConfig;
import com.github.paganini2008.springplayer.gateway.route.model.RouteFile;
import com.github.paganini2008.springplayer.gateway.route.pojo.FlushOperationDTO;
import com.github.paganini2008.springplayer.gateway.route.pojo.RouteConfigDTO;
import com.github.paganini2008.springplayer.gateway.route.pojo.RouteConfigGroupDTO;
import com.github.paganini2008.springplayer.gateway.route.pojo.RouteContentDTO;
import com.github.paganini2008.springplayer.gateway.route.service.RouteConfigManangerService;
import com.github.paganini2008.springplayer.gateway.route.service.RouteConfigPublishService;

import reactor.core.publisher.Mono;

/**
 * 
 * GatewayRouteEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@CrossOrigin(origins = "*")
@Validated
@RestController
@RequestMapping("/gateway-admin")
public class GatewayRouteEndpoint {

	@Autowired
	private RouteConfigManangerService routeConfigManagerService;

	@Autowired
	private RouteConfigPublishService routeConfigPublishService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 获取缓存中的路由配置
	 * 
	 * @return
	 */
	@GetMapping("/routes")
	public Mono<ApiResult<Map<Object, Object>>> routes() {
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(REDIS_KEY_ROUTE);
		return Mono.just(ApiResult.ok(entries));
	}

	/**
	 * 清理缓存中的路由配置
	 * 
	 * @return
	 */
	@PostMapping("/route/flush")
	public Mono<ApiResult<String>> flushRoutes(@Validated @RequestBody FlushOperationDTO dto) {
		if (ArrayUtils.isNotEmpty(dto.getGroupNames())) {
			Arrays.stream(dto.getGroupNames()).forEach(groupName -> {
				List<RouteConfig> list = routeConfigManagerService.findRouteConfigByGroupName(groupName);
				list.forEach(rc -> {
					redisTemplate.opsForHash().delete(REDIS_KEY_ROUTE, rc.getServiceId());
				});
			});
		}
		if (dto.isCascade()) {
			ApplicationContextUtils.publishEvent(new RefreshRoutesEvent(this));
		}
		return Mono.just(ApiResult.ok("清理路由成功"));
	}

	/**
	 * 保存路由配置信息
	 * 
	 * @param configDTOs
	 * @return
	 */
	@PostMapping("/route/save")
	public Mono<ApiResult<String>> batchSaveRoute(@RequestBody RouteConfigGroupDTO dto) throws Exception {
		routeConfigManagerService.batchSaveRouteConfig(dto.getGroupName(), dto.getConfigs());
		return Mono.just(ApiResult.ok("保存路由配置成功"));
	}

	/**
	 * 保存路由配置信息
	 * 
	 * @param routeContent
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/route/createNew")
	public Mono<ApiResult<String>> createNewRouteFile(@Validated @RequestBody Mono<RouteContentDTO> requestBody) throws Exception {
		return requestBody.map(routeContent -> {
			if (routeConfigManagerService.checkRouteFileExists(routeContent.getName())) {
				throw new IllegalStateException("此文件名已存在！");
			}
			try {
				routeConfigManagerService.loadFromText(routeContent.getName(), routeContent.getRule(), routeContent.getFormat());
			} catch (Exception e) {
				throw new IllegalStateException("保存配置失败: " + e.getMessage(), e);
			}
			return ApiResult.ok("保存路由配置成功");
		});
	}

	/**
	 * 保存路由配置信息(从纯文本)
	 * 
	 * @return
	 */
	@PostMapping("/route/load")
	public Mono<ApiResult<String>> loadRouteFile(@Validated @RequestBody Mono<RouteContentDTO> requestBody) throws Exception {
		return requestBody.map(routeContent -> {
			try {
				routeConfigManagerService.loadFromText(routeContent.getName(), routeContent.getRule(), routeContent.getFormat());
			} catch (Exception e) {
				throw new IllegalStateException("保存配置失败: " + e.getMessage(), e);
			}
			return ApiResult.ok("保存路由配置成功");
		});

	}

	/**
	 * 上传Yml文件
	 * 
	 * @param filePart
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/route/upload")
	public Mono<ApiResult<String>> uploadRouteFile(@RequestPart("file") FilePart filePart,
			@RequestParam(name = "format", required = false) String format) throws Exception {
		String fileName = filePart.filename();
		String extension = StringUtils.isNotBlank(format) ? format : FilenameUtils.getExtension(fileName);
		final Path tempFile = Files.createTempFile("upload_", "." + extension);
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
		DataBufferUtils.write(filePart.content(), channel, 0).doOnComplete(() -> {
			try {
				routeConfigManagerService.loadFromFile(fileName, tempFile.toFile(), extension);
			} catch (Exception e) {
				throw new IllegalStateException("上传配置失败", e);
			}
		}).subscribe();
		return Mono.just(ApiResult.ok("上传路由配置文件成功"));
	}

	/**
	 * 删除路由配置文件
	 * 
	 * @param rfId
	 * @return
	 */
	@DeleteMapping("/route/delete/{rfId}")
	public Mono<ApiResult<String>> deleteRouteFile(@PathVariable("rfId") Long rfId,
			@RequestParam(name = "sync", required = false, defaultValue = "false") boolean sync) {
		routeConfigManagerService.deleteRouteFileById(rfId, sync);
		return Mono.just(ApiResult.ok("删除路由配置文件成功"));
	}

	/**
	 * 发布路由
	 * 
	 * @param serviceId
	 * @param refresh
	 * @return
	 */
	@PostMapping(value = { "/route/publish", "/route/publish/{groupName}" })
	public Mono<ApiResult<String>> publishRoutes(@PathVariable(name = "groupName", required = false) String groupName,
			@RequestParam(name = "refresh", defaultValue = "true", required = false) boolean refresh) {
		if (StringUtils.isNotBlank(groupName)) {
			routeConfigPublishService.publishGroup(groupName, refresh);
		} else {
			routeConfigPublishService.publishAll(refresh);
		}
		ApplicationContextUtils.publishEvent(
				StringUtils.isNotBlank(groupName) ? new RoutePublishEvent(this, groupName) : new RoutePublishEvent(this, "ALL"));
		return Mono.just(ApiResult.ok("发布路由成功"));
	}

	/**
	 * 路由配置文件列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/route/files")
	public Mono<ApiResult<List<RouteFile>>> findRouteFile() throws Exception {
		List<RouteFile> routeFiles = routeConfigManagerService.findRouteFile();
		return Mono.just(ApiResult.ok(routeFiles));
	}

	/**
	 * 获取路由详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/route/file/{id}")
	public Mono<ApiResult<RouteFile>> getRouteFileById(@PathVariable("id") Long id) throws Exception {
		RouteFile rf = routeConfigManagerService.getRouteFileById(id);
		return Mono.just(ApiResult.ok(rf));
	}

	/**
	 * 获取组下得路由配置
	 * 
	 * @param groupName
	 * @return
	 */
	@GetMapping("/routes/{groupName}")
	public Mono<ApiResult<List<RouteConfigDTO>>> findRouteConfigByGroupName(@PathVariable("groupName") String groupName) {
		List<RouteConfig> configList = routeConfigManagerService.findRouteConfigByGroupName(groupName);
		List<RouteConfigDTO> dataList = configList.stream().map(rc -> JacksonUtils.parseJson(rc.getRule(), RouteConfigDTO.class))
				.collect(Collectors.toList());
		return Mono.just(ApiResult.ok(dataList));
	}

}
