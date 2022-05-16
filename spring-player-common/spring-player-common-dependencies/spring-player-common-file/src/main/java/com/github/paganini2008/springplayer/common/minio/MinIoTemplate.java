package com.github.paganini2008.springplayer.common.minio;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.io.IOUtils;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;

/**
 * 
 * MinIoTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MinIoTemplate implements InitializingBean {

	public MinIoTemplate(MinIoProperties config) {
		this.config = config;
	}

	private MinIoProperties config;
	private MinioClient minioClient;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.minioClient = MinioClient.builder().endpoint(config.getUrl()).credentials(config.getAccessKey(), config.getSecretKey())
				.build();

	}

	@SneakyThrows(Exception.class)
	public void createBucket(String bucketName) {
		boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		if (!isExist) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		}
	}

	@SneakyThrows(Exception.class)
	public List<Bucket> getAllBuckets() {
		return minioClient.listBuckets();
	}

	@SneakyThrows(Exception.class)
	public void upload(String bucketName, String fileName, InputStream content) {
		if (StringUtils.isBlank(bucketName)) {
			bucketName = config.getBucketName();
		}
		minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(content, content.available(), -1).build());
	}

	@SneakyThrows(Exception.class)
	public void deleteFile(String bucketName, String fileName) {
		if (StringUtils.isBlank(bucketName)) {
			bucketName = config.getBucketName();
		}
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
	}

	@SneakyThrows(Exception.class)
	public String getFileUrl(String bucketName, String fileName) {
		if (StringUtils.isBlank(bucketName)) {
			bucketName = config.getBucketName();
		}
		return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(fileName)
				.expiry(24, TimeUnit.HOURS).build());
	}

	@SneakyThrows(Exception.class)
	public void saveAs(String bucketName, String fileName, OutputStream output) {
		if (StringUtils.isBlank(bucketName)) {
			bucketName = config.getBucketName();
		}
		InputStream input = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
		try {
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	public MinioClient getMinioClient() {
		return minioClient;
	}

}
