package com.github.paganini2008.springplayer.codegen;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 
 * CodeGeneratorMojo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Mojo(name = "codegen", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CodeGeneratorMojo extends AbstractMojo {

	@Parameter(property = "author")
	private String author = "Fred Feng";

	@Parameter(property = "jdbcUrl")
	private String jdbcUrl;

	@Parameter(property = "driverClassName")
	private String driverClassName;

	@Parameter(property = "username")
	private String username;

	@Parameter(property = "password")
	private String password;

	@Parameter(property = "dbType")
	private String dbType = "mysql";

	@Parameter(property = "packageName")
	private String packageName = "com.yl.platform";

	@Parameter(property = "includedTables")
	private String includedTables;

	@Parameter(property = "excludedTables")
	private String excludedTables;

	@Parameter(property = "tablePrefix")
	private String tablePrefix;

	@Parameter(property = "tableFieldAnnotationEnabled")
	private boolean tableFieldAnnotationEnabled = true;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			validate();
			process();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void validate() {
		validate(author, "Null parameter: jdbcUrl");
		validate(driverClassName, "Null parameter: driverClassName");
		validate(username, "Null parameter: username");
		validate(password, "Null parameter: password");
		validate(dbType, "Null parameter: dbType");
		validate(packageName, "Null parameter: packageName");
	}
	
	private void validate(String arg, String msg) {
		if (StringUtils.isBlank(arg)) {
			throw new IllegalArgumentException(msg);
		}
	}

	private void process() {
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(FileUtils.getFile(new File(projectPath), "src", "main", "java").getAbsolutePath());
		gc.setAuthor(author);
		gc.setOpen(false);
		gc.setFileOverride(false);
		gc.setServiceName("%sService");
		gc.setIdType(IdType.AUTO);
		gc.setDateType(DateType.TIME_PACK);
		gc.setBaseResultMap(true);
		gc.setBaseColumnList(true);
		gc.setSwagger2(false);

		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(jdbcUrl);
		dsc.setDriverName(driverClassName);
		dsc.setUsername(username);
		dsc.setPassword(password);
		dsc.setDbType(DbType.getDbType(dbType));
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(packageName);
		pc.setController("controller");
		pc.setEntity("entity");
		pc.setService("service");
		pc.setMapper("mapper");
		mpg.setPackageInfo(pc);

		mpg.setTemplateEngine(new FreemarkerTemplateEngine());

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		if (StringUtils.isNotBlank(includedTables)) {
			strategy.setInclude(includedTables.split(","));
		}
		if (StringUtils.isNotBlank(excludedTables)) {
			strategy.setExclude(excludedTables.split(","));
		}
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setEntityTableFieldAnnotationEnable(tableFieldAnnotationEnabled);
		if (StringUtils.isNotBlank(tablePrefix)) {
			strategy.setTablePrefix(tablePrefix);
		}
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setEntityLombokModel(true);

		strategy.setRestControllerStyle(true);
		strategy.setControllerMappingHyphenStyle(true);

		mpg.setStrategy(strategy);

		// 执行
		mpg.execute();
	}

}
