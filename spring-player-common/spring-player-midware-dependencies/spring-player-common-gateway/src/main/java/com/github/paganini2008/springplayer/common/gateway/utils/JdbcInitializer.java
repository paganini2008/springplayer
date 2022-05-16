package com.github.paganini2008.springplayer.common.gateway.utils;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.io.IOUtils;
import com.github.paganini2008.devtools.jdbc.JdbcUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * JdbcInitializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class JdbcInitializer implements InitializingBean, Ordered {

	private final DataSource dataSource;
	private final String[] tableNames;

	@Value("${spring.cloud.gateway.route.management.createTable:false}")
	private boolean forceCreated;

	@Override
	public void afterPropertiesSet() throws Exception {
		for (String tableName : tableNames) {
			createTableIfPresent(tableName, String.format("db/%s.sql", tableName));
		}
	}

	private void createTableIfPresent(String tableName, String path) throws Exception {
		ClassPathResource resource = new ClassPathResource(path);
		String sqlText = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
		if (StringUtils.isNotBlank(sqlText)) {
			List<String> sqls = StringUtils.split(sqlText, ";");
			sqls.stream().filter(sql -> StringUtils.isNotBlank(sql)).forEach(sql -> {
				Connection connection = null;
				try {
					connection = dataSource.getConnection();
					if (forceCreated || !JdbcUtils.existsTable(connection, null, tableName)) {
						JdbcUtils.update(connection, sql);
						log.info("Execute sql: " + sql);
					}
				} catch (SQLException e) {
					log.error(e.getMessage(), e);
				} finally {
					JdbcUtils.closeQuietly(connection);
				}

			});
		}
	}

	@Override
	public int getOrder() {
		return 1000;
	}

}
