package com.github.paganini2008.springplayer.common.gateway.route;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.io.FileSystemResource;
import org.yaml.snakeyaml.Yaml;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * YmlRouteConfigReader
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class YmlRouteConfigReader implements RouteConfigReader {

	@Override
	public RouteConfigDTO[] load(String text) throws Exception {
		if (StringUtils.isBlank(text)) {
			return new RouteConfigDTO[0];
		}
		Yaml yaml = new Yaml();
		Map map = (Map) yaml.load(text);
		if (map == null) {
			throw new IllegalStateException("错误格式的文本");
		}
		return parse(map);
	}

	@Override
	public RouteConfigDTO[] load(File file) throws Exception {
		Yaml yaml = new Yaml();
		FileSystemResource resource = new FileSystemResource(file);
		if (resource.exists()) {
			try (InputStream in = resource.getInputStream()) {
				Map map = (Map) yaml.load(in);
				return parse(map);
			}
		}
		throw new FileNotFoundException("此文件不存在: " + file.getName());
	}

	private RouteConfigDTO[] parse(Map map) {
		List list = (List) getValueRecursively(map, "spring.cloud.gateway.routes");
		if (CollectionUtils.isEmpty(list)) {
			return new RouteConfigDTO[0];
		}
		JSONArray jsonArray = JSONArray.fromObject(list);
		JSONArray copy = JSONArray.fromObject(list);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject rule = jsonArray.getJSONObject(i);
			adjust("predicates", rule, i, copy);
			adjust("filters", rule, i, copy);
		}
		return JacksonUtils.parseJson(copy.toString(), RouteConfigDTO[].class);
	}

	private void adjust(String key, JSONObject rule, int ruleIndex, JSONArray copy) {
		if (rule.containsKey(key)) {
			JSONArray predicates = rule.getJSONArray(key);
			if (CollectionUtils.isNotEmpty(predicates)) {
				for (int j = 0; j < predicates.size(); j++) {
					Object predicateObject = predicates.get(j);
					if (predicateObject == null || predicateObject instanceof Map) {
						continue;
					}
					JSONArray copyPredicates = copy.getJSONObject(ruleIndex).getJSONArray(key);
					String text = copyPredicates.get(j).toString();
					if (StringUtils.isNotBlank(text)) {
						copyPredicates.remove(j);
						copyPredicates.add(j, Collections.singletonMap("text", text));
					}
				}
			}
		}
	}

	private Object getValueRecursively(Map map, String key) {
		String[] keys = key.split("\\.");
		if (ArrayUtils.isNotEmpty(keys)) {
			return getValueRecursively(map, keys, 0);
		}
		return null;
	}

	private Object getValueRecursively(Map map, String[] keys, int index) {
		if (index == keys.length - 1) {
			return map.get(keys[index]);
		}
		Map subMap = (Map) map.get(keys[index]);
		return MapUtils.isNotEmpty(subMap) ? getValueRecursively(subMap, keys, index + 1) : null;
	}

	public static void main(String[] args) throws Exception {
		YmlRouteConfigReader configReader = new YmlRouteConfigReader();
		RouteConfigDTO[] dtos = configReader.load(new File("d:/work/demo.yml"));
		System.out.println(Arrays.toString(dtos));
	}

}
