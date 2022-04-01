package com.github.paganini2008.springplayer.common.messenger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;

/**
 * 
 * DirectMultipartFile
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class DirectMultipartFile implements MultipartFile {

	public DirectMultipartFile(InputStream input, String name) {
		this.input = input;
		this.name = name;
	}

	private final InputStream input;
	private final String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return name;
	}

	@Override
	public String getContentType() {
		return MediaType.APPLICATION_OCTET_STREAM_VALUE;
	}

	@SneakyThrows
	@Override
	public boolean isEmpty() {
		return input.available() == 0;
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return FileCopyUtils.copyToByteArray(getInputStream());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return input;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		FileCopyUtils.copy(getInputStream(), Files.newOutputStream(dest.toPath()));
	}

}
