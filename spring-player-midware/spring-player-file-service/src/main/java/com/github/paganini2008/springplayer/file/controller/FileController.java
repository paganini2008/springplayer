package com.github.paganini2008.springplayer.file.controller;

import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.io.IOUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.file.FileStore;

import cn.hutool.http.server.HttpServerResponse;

/**
 * 
 * FileController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequestMapping("/file")
@RestController
public class FileController {

	@Autowired
	private FileStore fileStore;

	@PostMapping("/upload")
	public ApiResult<String> upload(@RequestParam("bucketName") String bucketName,
			@RequestParam(name = "fileName", required = false) String fileName, @RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("Empty uploading body");
		}
		fileStore.upload(bucketName, StringUtils.isNotBlank(fileName) ? fileName : file.getOriginalFilename(), file.getInputStream());
		return ApiResult.ok("上传成功");
	}

	@GetMapping("/url/{bucketName}/{fileName}")
	public ApiResult<String> getUrl(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName)
			throws Exception {
		String url = fileStore.getUrl(bucketName, fileName);
		return ApiResult.ok(url);
	}

	@GetMapping("/download/{bucketName}/{fileName}")
	public void download(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName,
			HttpServerResponse response) throws Exception {
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		OutputStream output = response.getOut();
		try {
			fileStore.download(bucketName, fileName, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

}
