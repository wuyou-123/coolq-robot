package com.wuyou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuyou.mapper.ClearMapper;
import com.wuyou.service.ClearService;

/**
 * 退群控制器
 * 
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Service
public class ClearServiceImpl implements ClearService {
	@Autowired
	ClearMapper mapper;

	@Override
	public void clearAllData(String groupId) {
		System.out.println("准备删除");
		mapper.clearBanMessage(groupId);
		System.out.println("删除禁言关键词");
		mapper.clearBlackUser(groupId);
		System.out.println("删除黑名单");
		mapper.clearManager(groupId);
		System.out.println("删除管理员");
		mapper.clearMessage(groupId);
		System.out.println("删除消息列表");
		mapper.clearStat(groupId);
		System.out.println("删除开关机状态");
	}

}
