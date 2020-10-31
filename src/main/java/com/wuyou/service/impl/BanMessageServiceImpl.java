package com.wuyou.service.impl;

import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.exception.UpdateException;
import com.wuyou.mapper.BanMessageMapper;
import com.wuyou.service.BanMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 禁言关键词控制器
 * 
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Service
public class BanMessageServiceImpl implements BanMessageService {
	private final BanMessageMapper mapper;

	@Autowired
	public BanMessageServiceImpl(BanMessageMapper mapper) {
		this.mapper = mapper;
	}
	/**
	 * 添加禁言关键词
	 * 
	 * @param groupId 群号
	 * @param message 禁言关键词内容
	 */
	@Override
	public void addBanMessage(String groupId, String message) {
		List<String> list = getAllByGroupId(groupId);
		if (!list.contains(message)) {
			int row = mapper.insertBanMessage(groupId, message);
			if (row != 1) {
				throw new UpdateException("插入数据时出现异常,请联系管理员");
			}
		} else {
			throw new ObjectExistedException("禁言关键词已存在!");
		}
	}

	/**
	 * 删除禁言关键词
	 * 
	 * @param groupId 群号
	 * @param message 禁言关键词
	 */
	@Override
	public void removeBanMessage(String groupId, String message) {
		int row = mapper.deleteBanMessage(groupId, message);
		if (row == 0) {
			throw new ObjectNotFoundException("未找到对应的消息");
		} else if (row != 1) {
			throw new UpdateException("更新数据时出现异常,请联系管理员");
		}
	}

	/**
	 * 查询所有禁言关键词
	 * 
	 * @param groupId 群号
	 * @return 禁言关键词集合
	 */
	@Override
	public List<String> getAllByGroupId(String groupId) {
		return mapper.findAllByGroupId(groupId);
	}
}
