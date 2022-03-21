package com.github.paganini2008.springplayer.common.file;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * RemoteFileService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteFileService", value = "spring-player-file-service")
public interface RemoteFileService {

	@PostMapping("/upload")
	ApiResult<String> upload(@RequestParam("bucketName") String bucketName,
			@RequestParam(name = "fileName", required = false) String fileName, @RequestParam("file") MultipartFile file);

	@GetMapping("/url/{bucketName}/{fileName}")
	ApiResult<String> getUrl(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName);

}
