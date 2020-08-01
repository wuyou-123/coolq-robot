package com.wuyou.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuyou.entity.Stat;
import com.wuyou.mapper.StatMapper;
import com.wuyou.service.StatService;

/**
 * 机器人开关机控制器
 * 
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Service
public class StatServiceImpl implements StatService {
	@Autowired
	StatMapper mapper;

	/**
	 * 开机/关机
	 * 
	 * @param groupId 群号
	 * @param stat    开关机状态,1为开机,0为关机
	 */
	@Override
	public void bootAndShutDown(String groupId, int stat) {
		Integer nowstat = mapper.findStat(groupId);
		if (nowstat == null && stat == 1) {
			mapper.bootAndShutDown(groupId, stat);
			return;
		}
		if (nowstat == stat) {
			return;
		}
		mapper.changeStat(groupId, stat);

	}

	@Override
	public int getStat(String groupId) {
		Integer num = mapper.findStat(groupId);
		return num == null ? -1 : num;
	}

	@Override
	public Map<String, Boolean> getAllStat() {
		List<Stat> stat = mapper.findAllStat();
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		for (Stat s : stat) {
			map.put(s.getGroupId(), s.getStat() == 1 ? true : false);
		}
		return map;
	}
}
