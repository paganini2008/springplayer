package com.github.paganini2008.springplayer.gateway.config;

import static org.springframework.cloud.gateway.config.HttpClientProperties.Pool.PoolType.DISABLED;
import static org.springframework.cloud.gateway.config.HttpClientProperties.Pool.PoolType.FIXED;

import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.ProxyProvider;

/**
 * 
 * NettyHttpClientConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(HttpClient.class)
public class NettyHttpClientConfig {

	@Bean
	public HttpClient gatewayHttpClient(HttpClientProperties properties,
			List<HttpClientCustomizer> customizers) {

		// configure pool resources
		ConnectionProvider connectionProvider = buildConnectionProvider(properties);

		HttpClient httpClient = HttpClient.create(connectionProvider)
				// TODO: move customizations to HttpClientCustomizers
				.httpResponseDecoder(spec -> {
					if (properties.getMaxHeaderSize() != null) {
						// cast to int is ok, since @Max is Integer.MAX_VALUE
						spec.maxHeaderSize(
								(int) properties.getMaxHeaderSize().toBytes());
					}
					if (properties.getMaxInitialLineLength() != null) {
						// cast to int is ok, since @Max is Integer.MAX_VALUE
						spec.maxInitialLineLength(
								(int) properties.getMaxInitialLineLength().toBytes());
					}
					return spec;
				}).tcpConfiguration(tcpClient -> {

					if (properties.getConnectTimeout() != null) {
						tcpClient = tcpClient.option(
								ChannelOption.CONNECT_TIMEOUT_MILLIS,
								properties.getConnectTimeout());
					}

					// configure proxy if proxy host is set.
					HttpClientProperties.Proxy proxy = properties.getProxy();

					if (StringUtils.hasText(proxy.getHost())) {

						tcpClient = tcpClient.proxy(proxySpec -> {
							ProxyProvider.Builder builder = proxySpec
									.type(ProxyProvider.Proxy.HTTP)
									.host(proxy.getHost());

							PropertyMapper map = PropertyMapper.get();

							map.from(proxy::getPort).whenNonNull().to(builder::port);
							map.from(proxy::getUsername).whenHasText()
									.to(builder::username);
							map.from(proxy::getPassword).whenHasText()
									.to(password -> builder.password(s -> password));
							map.from(proxy::getNonProxyHostsPattern).whenHasText()
									.to(builder::nonProxyHosts);
						});
					}
					return tcpClient;
				});

		HttpClientProperties.Ssl ssl = properties.getSsl();
		if ((ssl.getKeyStore() != null && ssl.getKeyStore().length() > 0)
				|| ssl.getTrustedX509CertificatesForTrustManager().length > 0
				|| ssl.isUseInsecureTrustManager()) {
			httpClient = httpClient.secure(sslContextSpec -> {
				// configure ssl
				SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

				X509Certificate[] trustedX509Certificates = ssl
						.getTrustedX509CertificatesForTrustManager();
				if (trustedX509Certificates.length > 0) {
					sslContextBuilder = sslContextBuilder
							.trustManager(trustedX509Certificates);
				}
				else if (ssl.isUseInsecureTrustManager()) {
					sslContextBuilder = sslContextBuilder
							.trustManager(InsecureTrustManagerFactory.INSTANCE);
				}

				try {
					sslContextBuilder = sslContextBuilder
							.keyManager(ssl.getKeyManagerFactory());
				}
				catch (Exception e) {
					log.error(e.getMessage(), e);
				}

				sslContextSpec.sslContext(sslContextBuilder)
						.defaultConfiguration(ssl.getDefaultConfigurationType())
						.handshakeTimeout(ssl.getHandshakeTimeout())
						.closeNotifyFlushTimeout(ssl.getCloseNotifyFlushTimeout())
						.closeNotifyReadTimeout(ssl.getCloseNotifyReadTimeout());
			});
		}

		if (properties.isWiretap()) {
			httpClient = httpClient.wiretap(true);
		}

		if (properties.isCompression()) {
			httpClient = httpClient.compress(true);
		}

		if (!CollectionUtils.isEmpty(customizers)) {
			customizers.sort(AnnotationAwareOrderComparator.INSTANCE);
			for (HttpClientCustomizer customizer : customizers) {
				httpClient = customizer.customize(httpClient);
			}
		}

		return httpClient;
	}

	private ConnectionProvider buildConnectionProvider(
			HttpClientProperties properties) {
		HttpClientProperties.Pool pool = properties.getPool();

		ConnectionProvider connectionProvider;
		if (pool.getType() == DISABLED) {
			connectionProvider = ConnectionProvider.newConnection();
		}
		else {
			// create either Fixed or Elastic pool
			ConnectionProvider.Builder builder = ConnectionProvider
					.builder(pool.getName());
			if (pool.getType() == FIXED) {
				builder.maxConnections(pool.getMaxConnections())
						.pendingAcquireMaxCount(-1).pendingAcquireTimeout(
								Duration.ofMillis(pool.getAcquireTimeout()));
			}
			else {
				// Elastic
				builder.maxConnections(Integer.MAX_VALUE)
						.pendingAcquireTimeout(Duration.ofMillis(0))
						.pendingAcquireMaxCount(-1);
			}

			if (pool.getMaxIdleTime() != null) {
				builder.maxIdleTime(pool.getMaxIdleTime());
			}
			if (pool.getMaxLifeTime() != null) {
				builder.maxLifeTime(pool.getMaxLifeTime());
			}
			builder.evictInBackground(pool.getEvictionInterval());
			builder.metrics(true); // show Netty connection metrics
			connectionProvider = builder.build();
		}
		return connectionProvider;
	}
	
}
