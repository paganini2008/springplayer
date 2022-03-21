package com.github.paganini2008.springplayer.common.minio;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.paganini2008.springplayer.common.file.FileStore;

import lombok.RequiredArgsConstructor;

/**
 * 
 * MinIoFileStore
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class MinIoFileStore implements FileStore {

	private final MinIoTemplate template;

	@Override
	public void upload(String bucketName, String fileName, InputStream content) {
		template.upload(bucketName, fileName, content);
	}

	@Override
	public void download(String bucketName, String fileName, OutputStream output) {
		template.saveAs(bucketName, fileName, output);
	}

	@Override
	public String getUrl(String bucketName, String fileName) {
		return template.getFileUrl(bucketName, fileName);
	}

}
