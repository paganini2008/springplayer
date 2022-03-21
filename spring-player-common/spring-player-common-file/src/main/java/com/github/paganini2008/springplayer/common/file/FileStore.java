package com.github.paganini2008.springplayer.common.file;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * FileStore
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface FileStore {

	void upload(String bucketName, String fileName, InputStream content);

	void download(String bucketName, String fileName, OutputStream output);

	String getUrl(String bucketName, String fileName);

}
