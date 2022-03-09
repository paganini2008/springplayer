package com.github.paganini2008.springplayer.channel.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.beans.PropertyUtils;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.devtools.jdbc.PageRequest;
import com.github.paganini2008.devtools.jdbc.PageResponse;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;
import com.github.paganini2008.springplayer.channel.model.Channel;
import com.github.paganini2008.springplayer.channel.model.ChannelSetting;
import com.github.paganini2008.springplayer.channel.pojo.ChannelDTO;
import com.github.paganini2008.springplayer.channel.pojo.ChannelQueryDTO;
import com.github.paganini2008.springplayer.channel.pojo.PageWrapper;
import com.github.paganini2008.springplayer.common.id.IdGenerator;

/**
 * 
 * ChannelManagerService
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Service
public class ChannelManagerService {

	@Value("${spring.profiles.active:default}")
	private String env;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ChannelSettingService channelSettingService;

	@Autowired
	private IdGenerator idGenerator;

	public long saveChannel(ChannelDTO dto) {
		Channel channel = null;
		if (dto.getId() != null) {
			channel = getChannelById(dto.getId());
		}
		if (channel == null) {
			channel = getChannelByName(dto.getName());
		}
		if (channel == null) {
			channel = new Channel();
			channel.setId(idGenerator.generateId());
			channel.setCreateTime(LocalDateTime.now());
		}
		channel.setChannelType(dto.getChannelType());
		channel.setUpdateTime(LocalDateTime.now());
		channelService.saveOrUpdate(channel);

		saveChannelSettings(dto, channel.getId());
		return channel.getId();
	}

	public boolean deleteChannel(long channelId) {
		deleteChannelSettings(channelId);
		return channelService.removeById(channelId);
	}

	private boolean deleteChannelSettings(long channelId) {
		LambdaQueryWrapper<ChannelSetting> query = Wrappers.<ChannelSetting>lambdaQuery();
		query = query.eq(ChannelSetting::getChannelId, channelId);
		return channelSettingService.remove(query);
	}

	private void saveChannelSettings(ChannelDTO dto, long channelId) {
		deleteChannelSettings(channelId);

		if (dto.getDingTalk() != null) {
			saveChannelSettings(dto.getDingTalk(), channelId);
		}
		if (dto.getEmail() != null) {
			saveChannelSettings(dto.getEmail(), channelId);
		}
		if (dto.getSms() != null) {
			saveChannelSettings(dto.getSms(), channelId);
		}
		if (dto.getWechat() != null) {
			saveChannelSettings(dto.getWechat(), channelId);
		}
	}

	private void saveChannelSettings(Object obj, long channelId) {
		Map<String, Object> data = PropertyUtils.convertToMap(obj);
		if (MapUtils.isEmpty(data)) {
			return;
		}
		LocalDateTime now = LocalDateTime.now();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (entry.getValue() != null) {
				ChannelSetting channelSetting = new ChannelSetting();
				channelSetting.setId(idGenerator.generateId());
				channelSetting.setChannelId(channelId);
				channelSetting.setName(entry.getKey());
				channelSetting.setValue(stringValue(entry.getValue()));
				channelSetting.setCreateTime(now);
				channelSetting.setUpdateTime(now);
				channelSettingService.save(channelSetting);
			}

		}

	}

	private String stringValue(Object value) {
		if (value instanceof Object[]) {
			return ArrayUtils.join((Object[]) value);
		} else {
			return value.toString();
		}
	}

	public Channel getChannelById(Long id) {
		return channelService.getById(id);
	}

	public Channel getChannelByName(String name) {
		LambdaQueryWrapper<Channel> query = Wrappers.<Channel>lambdaQuery();
		return channelService.getOne(query);
	}

	public PageWrapper<Channel> pageForChannel(ChannelQueryDTO dto) {
		LambdaQueryWrapper<Channel> query = Wrappers.<Channel>lambdaQuery();
		if (dto.getChannelType() != null) {
			query = query.eq(Channel::getChannelType, dto.getChannelType());
		}
		ResultSetSlice<Channel> rss = channelService.slice(query);
		PageResponse<Channel> pageResponse = rss.list(PageRequest.of(dto.getPage(), dto.getSize()));
		PageWrapper<Channel> page = new PageWrapper<>();
		page.setPage(dto.getPage());
		page.setSize(dto.getSize());
		if (pageResponse.isEmpty()) {
			return page;
		}
		page.setContent(pageResponse.getContent());
		page.setTotalPages(pageResponse.getTotalPages());
		page.setTotalRecords(pageResponse.getTotalRecords());
		return page;
	}

}
