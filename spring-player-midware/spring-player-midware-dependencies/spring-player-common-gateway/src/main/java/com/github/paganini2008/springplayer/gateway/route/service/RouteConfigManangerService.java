package com.github.paganini2008.springplayer.gateway.route.service;

import static com.github.paganini2008.springplayer.gateway.GatewayConstants.REDIS_KEY_ROUTE;
import static com.github.paganini2008.springplayer.gateway.GatewayConstants.SENTINEL_GATEWAY_FLOW_RULE;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.id.IdGenerator;
import com.github.paganini2008.springplayer.gateway.route.RouteConfigReader;
import com.github.paganini2008.springplayer.gateway.route.RouteConfigUtils;
import com.github.paganini2008.springplayer.gateway.route.model.RouteConfig;
import com.github.paganini2008.springplayer.gateway.route.model.RouteFile;
import com.github.paganini2008.springplayer.gateway.route.pojo.RouteConfigDTO;
import com.github.paganini2008.springplayer.gateway.sentinel.pojo.ApiPathPredicateItemDTO;
import com.github.paganini2008.springplayer.gateway.sentinel.pojo.SentinelRuleDTO;
import com.github.paganini2008.springplayer.gateway.sentinel.service.SentinelRuleManagerService;
import com.github.paganini2008.springplayer.sentinel.RuleType;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * RouteConfigManangerService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Service
public class RouteConfigManangerService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RouteConfigService routeConfigService;

	@Autowired
	private RouteFileService routeFileService;

	@Autowired
	private IdGenerator idGenerator;

	@Qualifier("ymlReader")
	@Autowired
	private RouteConfigReader ymlReader;

	@Qualifier("jsonReader")
	@Autowired
	private RouteConfigReader jsonReader;

	@Value("${spring.profiles.active:default}")
	private String env;

	@Autowired
	private SentinelRuleManagerService sentinelRuleManagerService;

	/**
	 * 保存多个路由配置
	 * 
	 * @param groupName  组名
	 * @param configDTOs 配置信息
	 */
	public void batchSaveRouteConfig(String groupName, RouteConfigDTO[] configDTOs) {
		if (ArrayUtils.isNotEmpty(configDTOs)) {
			for (RouteConfigDTO configDTO : configDTOs) {
				saveRouteConfig(groupName, configDTO);
			}
			List<RouteConfig> rcs = findRouteConfigByGroupName(groupName);
			rcs.stream().filter(rc -> checkDeleted(rc.getServiceId(), configDTOs))
					.forEach(rc -> deleteRouteConfigByServiceId(groupName, rc.getServiceId(), true));
		}
	}

	/**
	 * 合并和导出
	 * 
	 * @param groupNames
	 */
	@SuppressWarnings("unchecked")
	public String mergeAndExport(String... groupNames) {
		List<RouteConfig> allRouteConfigs = new ArrayList<>();
		Arrays.stream(groupNames).forEach(groupName -> {
			allRouteConfigs.addAll(findRouteConfigByGroupName(groupName));
		});
		List<JSONObject> routeList = allRouteConfigs.stream().map(rc -> createJsonObject(rc.getRule())).collect(Collectors.toList());
		final JSONObject root = new JSONObject();
		root.put("spring", new JSONObject());
		JSONObject spring = root.getJSONObject("spring");
		spring.put("cloud", new JSONObject());
		JSONObject cloud = spring.getJSONObject("cloud");
		cloud.put("gateway", new JSONObject());
		JSONObject gateway = cloud.getJSONObject("gateway");
		gateway.put("routes", routeList);
		Yaml yaml = new Yaml();
		Map<String, Object> map = (Map<String, Object>) yaml.load(root.toString());
		return yaml.dumpAsMap(map);
	}

	private JSONObject createJsonObject(String rule) {
		JSONObject json = JSONObject.fromObject(rule);
		JSONObject copy = JSONObject.fromObject(rule);
		JSONArray predicates = json.getJSONArray("predicates");
		if (predicates != null && predicates.size() > 0) {
			adjustJsonArray(predicates, copy.getJSONArray("predicates"));
		} else {
			copy.remove("predicates");
		}
		JSONArray filters = json.getJSONArray("filters");
		if (filters != null && filters.size() > 0) {
			adjustJsonArray(filters, copy.getJSONArray("filters"));
		} else {
			copy.remove("filters");
		}
		JSONObject metadata = json.getJSONObject("metadata");
		if (metadata == null || metadata.isEmpty()) {
			copy.remove("metadata");
		}
		int order = json.getInt("order");
		if (order == 0) {
			copy.remove("order");
		}
		return copy;
	}

	private void adjustJsonArray(JSONArray jsonArray, JSONArray copy) {
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if (jsonObject.has("text")) {
				String text = jsonObject.getString("text");
				copy.remove(i);
				copy.add(text);
			}
		}
	}

	/**
	 * 判断此ServiceId被删除
	 * 
	 * @param serviceId
	 * @param configDTOs
	 * @return
	 */
	private boolean checkDeleted(String serviceId, RouteConfigDTO[] configDTOs) {
		return Arrays.stream(configDTOs).noneMatch(c -> serviceId.equals(c.getId()));
	}

	/**
	 * 保存路由配置
	 * 
	 * @param groupName 组名
	 * @param configDTO 配置信息
	 */
	public void saveRouteConfig(String groupName, RouteConfigDTO configDTO) {
		final String serviceId = configDTO.getId();
		boolean update = true;
		RouteConfig routeConfig = null;

		if (StringUtils.isNotBlank(serviceId)) {
			routeConfig = findRouteConfigByServiceId(serviceId);
			if (routeConfig != null && !StringUtils.equals(groupName, routeConfig.getGroupName())) {
				log.warn("在其他组下找到相同的服务名：{}，当前组名：{}和原组名：{}", serviceId, groupName, routeConfig.getGroupName());
			}
		}
		if (routeConfig == null) {
			routeConfig = new RouteConfig();
			routeConfig.setId(idGenerator.generateId());
			routeConfig.setCreateTime(LocalDateTime.now());
			update = false;
		}
		routeConfig.setServiceId(serviceId);
		routeConfig.setGroupName(groupName);
		routeConfig.setEnv(env);
		routeConfig.setRule(JacksonUtils.toJsonString(configDTO));
		routeConfig.setUpdateTime(LocalDateTime.now());
		routeConfigService.saveOrUpdate(routeConfig);
		log.info("{}路由配置 serviceId: {}", (update ? "更新" : "新增"), routeConfig.getServiceId());
	}

	/**
	 * 根据服务名查找路由配置
	 * 
	 * @param serviceId
	 * @return
	 */
	public RouteConfig findRouteConfigByServiceId(String serviceId) {
		LambdaQueryWrapper<RouteConfig> query = Wrappers.<RouteConfig>lambdaQuery().eq(RouteConfig::getEnv, env)
				.eq(RouteConfig::getServiceId, serviceId);
		return routeConfigService.getOne(query);
	}

	/**
	 * 根据组名获取路由配置列表
	 * 
	 * @param groupName
	 * @return
	 */
	public List<RouteConfig> findRouteConfigByGroupName(String groupName) {
		LambdaQueryWrapper<RouteConfig> query = Wrappers.<RouteConfig>lambdaQuery().eq(RouteConfig::getEnv, env)
				.eq(RouteConfig::getGroupName, groupName);
		return routeConfigService.list(query);
	}

	/**
	 * 根据服务名删除路由配置
	 * 
	 * @param serviceId
	 */
	public void deleteRouteConfigByServiceId(String groupName, String serviceId, boolean sync) {
		LambdaQueryWrapper<RouteConfig> query = Wrappers.<RouteConfig>lambdaQuery().eq(RouteConfig::getEnv, env)
				.eq(RouteConfig::getGroupName, groupName).eq(RouteConfig::getServiceId, serviceId);
		boolean updated = routeConfigService.remove(query);
		if (updated) {
			if (sync) {
				redisTemplate.opsForHash().delete(REDIS_KEY_ROUTE, serviceId);
				log.info("级联删除路由配置： " + serviceId);
			}
			log.info("删除路由配置： " + serviceId);
		}
	}

	/**
	 * 根据配置文件ID删除配置文件信息
	 * 
	 * @param rfId
	 */
	public void deleteRouteFileById(Long rfId, boolean sync) {
		RouteFile routeFile = getRouteFileById(rfId);
		if (routeFile != null) {
			String groupName = routeFile.getFileName();
			List<RouteConfig> configs = findRouteConfigByGroupName(groupName);
			if (CollectionUtils.isNotEmpty(configs)) {
				for (RouteConfig config : configs) {
					deleteRouteConfigByServiceId(groupName, config.getServiceId(), sync);
				}
			}
			if (routeFileService.removeById(rfId)) {
				log.info("删除路由配置文件：" + routeFile.getFileName());
			}
		}
	}

	/**
	 * 从文件加载
	 * 
	 * @param file   文件路径
	 * @param format 文件格式
	 * @throws Exception
	 */
	public void loadFromFile(String groupName, File file, String format) throws Exception {
		if (StringUtils.isBlank(format)) {
			throw new IllegalArgumentException("未识别的文件格式：" + file.getName());
		}
		RouteConfigDTO[] routeConfigDTOs;
		try {
			switch (format) {
			case "json":
				routeConfigDTOs = jsonReader.load(file);
				break;
			case "yml":
			case "yaml":
				routeConfigDTOs = ymlReader.load(file);
				break;
			default:
				throw new UnsupportedOperationException("尚未支持的文件格式：" + format);
			}
			batchSaveRouteConfig(groupName, routeConfigDTOs);
			String text = FileUtils.readFileToString(file, "utf-8");
			saveRouteFile(groupName, text, format);

			cascadeSaveSentinelRules(routeConfigDTOs);
		} finally {
			FileUtils.forceDelete(file);
		}
	}

	/**
	 * 从纯文本加载
	 * 
	 * @param name
	 * @param text
	 * @param format
	 * @throws Exception
	 */
	public void loadFromText(String groupName, String text, String format) throws Exception {
		RouteConfigDTO[] routeConfigDTOs;
		switch (format) {
		case "json":
			routeConfigDTOs = jsonReader.load(text);
			break;
		case "yml":
		case "yaml":
			routeConfigDTOs = ymlReader.load(text);
			break;
		default:
			throw new UnsupportedOperationException("尚未支持的文件格式：" + format);
		}
		batchSaveRouteConfig(groupName, routeConfigDTOs);
		saveRouteFile(groupName, text, format);
		cascadeSaveSentinelRules(routeConfigDTOs);
	}

	@SuppressWarnings("all")
	private void cascadeSaveSentinelRules(RouteConfigDTO[] routeConfigDTOs) {
		Arrays.stream(routeConfigDTOs).forEach(rc -> {
			Map<String, Object> metadata = rc.getMetadata();
			if (MapUtils.isNotEmpty(metadata)) {
				Map<String, Object> sentinelConf = (Map<String, Object>) metadata.get("sentinel");
				if (MapUtils.isNotEmpty(sentinelConf)) {
					Map<String, Object> flowConfig = (Map<String, Object>) sentinelConf.get("flow");
					if (MapUtils.isNotEmpty(flowConfig)) {
						flowConfig.put("resource", rc.getId());
						List<Map<String, Object>> apiList = (List<Map<String, Object>>) flowConfig.get("api");
						if (CollectionUtils.isEmpty(apiList)) {
							String pathPattern = RouteConfigUtils.extractPath(rc);
							if (StringUtils.isBlank(pathPattern)) {
								throw new IllegalArgumentException("请指定要流控的API");
							}
							flowConfig.put("api", Arrays
									.asList(new ApiPathPredicateItemDTO(pathPattern, SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX)));
						}
						saveSentinelRule(RuleType.GATEWAY_FLOW, flowConfig, rc.getId());
					}
				}
			}
		});

	}

	private void saveSentinelRule(RuleType ruleType, Map<String, Object> config, String serviceId) {
		SentinelRuleDTO sentinelRule = new SentinelRuleDTO();
		sentinelRule.setRuleKey(String.format("%s:%s", SENTINEL_GATEWAY_FLOW_RULE, serviceId));
		sentinelRule.setRuleType(ruleType);
		sentinelRule.setRule(JacksonUtils.toJsonString(Arrays.asList(config)));
		sentinelRuleManagerService.saveSentinelRule(sentinelRule);

	}

	public boolean checkRouteFileExists(String name) {
		LambdaQueryWrapper<RouteFile> query = Wrappers.<RouteFile>lambdaQuery().eq(RouteFile::getFileName, name).eq(RouteFile::getEnv, env);
		return routeFileService.count(query) > 0;
	}

	public void saveRouteFile(String groupName, String text, String format) {
		LambdaQueryWrapper<RouteFile> query = Wrappers.<RouteFile>lambdaQuery().eq(RouteFile::getFileName, groupName).eq(RouteFile::getEnv,
				env);
		RouteFile routeFile = routeFileService.getOne(query);
		if (routeFile == null) {
			routeFile = new RouteFile();
			routeFile.setId(idGenerator.generateId());
			routeFile.setCreateTime(LocalDateTime.now());
		}
		routeFile.setFileName(groupName);
		routeFile.setEnv(env);
		routeFile.setContent(text);
		routeFile.setFormat(format);
		routeFile.setUpdateTime(LocalDateTime.now());
		routeFileService.saveOrUpdate(routeFile);
		log.info("保存路由文件信息：" + groupName);
	}

	public List<RouteFile> findRouteFile() {
		LambdaQueryWrapper<RouteFile> query = Wrappers.<RouteFile>lambdaQuery().eq(RouteFile::getEnv, env)
				.orderByDesc(RouteFile::getUpdateTime);
		return routeFileService.list(query);
	}

	public RouteFile getRouteFileByName(String name) {
		LambdaQueryWrapper<RouteFile> query = Wrappers.<RouteFile>lambdaQuery().eq(RouteFile::getEnv, env).eq(RouteFile::getFileName, name);
		return routeFileService.getOne(query);
	}

	public RouteFile getRouteFileById(Long id) {
		return routeFileService.getById(id);
	}

}
