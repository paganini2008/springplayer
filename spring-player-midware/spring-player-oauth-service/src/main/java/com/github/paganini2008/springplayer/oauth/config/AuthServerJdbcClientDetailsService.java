package com.github.paganini2008.springplayer.oauth.config;

import javax.sql.DataSource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.security.info.OauthConstants;
import com.github.paganini2008.springplayer.webmvc.RequestHeaderContextHolder;

/**
 * 
 * AuthServerJdbcClientDetailsService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class AuthServerJdbcClientDetailsService extends JdbcClientDetailsService{

	public AuthServerJdbcClientDetailsService(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	@Cacheable(value = "oauth:client:details", key = "#clientId", unless = "#result == null")
	public ClientDetails loadClientByClientId(String clientId) {
		String sql = String.format(OauthConstants.DEFAULT_SELECT_STATEMENT, RequestHeaderContextHolder.getTenantId());
		super.setSelectClientDetailsSql(sql);
		return super.loadClientByClientId(clientId);
	}

}