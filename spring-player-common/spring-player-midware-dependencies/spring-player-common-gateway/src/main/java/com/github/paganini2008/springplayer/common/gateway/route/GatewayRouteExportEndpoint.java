package com.github.paganini2008.springplayer.common.gateway.route;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.CharsetUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.gateway.route.pojo.ExportOperationDTO;
import com.github.paganini2008.springplayer.common.gateway.route.service.RouteConfigManangerService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 * GatewayRouteExportEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@Validated
@RequestMapping("/gateway-admin")
@RestController
public class GatewayRouteExportEndpoint {

	@Autowired
	private RouteConfigManangerService routeConfigManagerService;

	/**
	 * 导出路由配置
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping("/route/export")
	public Mono<Void> export(@Validated @RequestBody ExportOperationDTO dto, ServerHttpResponse response) throws Exception {
		String ymlString = ArrayUtils.isNotEmpty(dto.getGroupNames()) ? routeConfigManagerService.mergeAndExport(dto.getGroupNames()) : "";
		String fileName = StringUtils.isNotBlank(dto.getFileName()) ? dto.getFileName() : (String) ArrayUtils.getLast(dto.getGroupNames());
		ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
		response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".yml");
		response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
		File tmpDir = new File(new File(System.getProperty("user.dir")), "tmp");
		FileUtils.forceMkdir(tmpDir);
		File output = new File(tmpDir, UUID.randomUUID().toString() + ".yml");
		FileUtils.writeStringToFile(output, ymlString, CharsetUtils.UTF_8);
		if (log.isTraceEnabled()) {
			log.trace("Create temporary file for exporting. File: {}, length: {}", output, output.length());
		}
		return zeroCopyResponse.writeWith(output, 0, output.length()).then(Mono.fromRunnable(() -> {
			if (output.exists()) {
				FileUtils.deleteQuietly(output);
			}
		}));
	}

}
