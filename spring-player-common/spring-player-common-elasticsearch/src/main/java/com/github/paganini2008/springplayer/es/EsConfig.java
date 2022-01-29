package com.github.paganini2008.springplayer.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * EsConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableConfigurationProperties({ EsProperties.class })
@Configuration(proxyBeanMethods = false)
public class EsConfig {

	@ConditionalOnMissingBean(name = "esHttpClient")
	@Bean(destroyMethod = "close")
	public RestHighLevelClient esHttpClient(EsProperties esProperties) {

		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(esProperties.getUsername(), esProperties.getPassword()));

		RestClientBuilder builder = RestClient.builder(new HttpHost(esProperties.getHost(), esProperties.getPort()))
				.setRequestConfigCallback(new RequestConfigCallback() {
					@Override
					public Builder customizeRequestConfig(Builder builder) {
						return builder.setConnectTimeout(esProperties.getConnectTimeout()).setSocketTimeout(esProperties.getSocketTimeout())
								.setConnectionRequestTimeout(esProperties.getConnectionRequestTimeout());
					}
				}).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
						httpClientBuilder.disableAuthCaching();
						return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					}
				});

		return new RestHighLevelClient(builder);
	}

}
