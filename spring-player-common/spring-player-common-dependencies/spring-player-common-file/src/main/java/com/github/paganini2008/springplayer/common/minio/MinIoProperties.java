package com.github.paganini2008.springplayer.common.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * MinIoProperties
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.player.minio")
public class MinIoProperties {
	
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
