package com.wuyou.service.impl;

import com.wuyou.entity.Message;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.exception.UpdateException;
import com.wuyou.mapper.MessageMapper;
import com.wuyou.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动回复控制器
 * 
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	MessageMapper mapper;

	/**
	 * 添加自动回复消息
	 * 
	 * @param groupId 群号
	 * @param message 消息内容
	 * @param answer  回复内容
	 */
	@Override
	public Integer addMessage(String groupId, String message, String answer) {
		String ans = getAnswerByMessage(groupId, message);
		if (ans == null || "".equals(ans)) {
			int row = mapper.insertMessage(groupId, message, answer);
			if (row != 1) {
				throw new UpdateException("插入数据时出现异常,请联系管理员");
			}
			return row;
		} else if (ans.equals(answer)) {
			throw new ObjectExistedException("此条消息已存在");
		} else {
			return changeMessage(groupId, message, answer);
		}
	}

	/**
	 * 删除自动回复消息
	 * 
	 * @param groupId 群号
	 * @param message 消息内容
	 */
	@Override
	public void removeMessage(String groupId, String message) {
		int row = mapper.deleteMessage(groupId, message);
		if (row == 0) {
			throw new ObjectNotFoundException("未找到对应的消息");
		} else if (row != 1) {
			throw new UpdateException("更新数据时出现异常,请联系管理员");
		}
	}

	/**
	 * 查询所有自动回复消息
	 * 
	 * @param groupId 群号
	 * @return 消息内容和回复内容的键值对
	 */
	@Override
	public Map<String, String> getAllByGroup(String groupId) {
		List<Message> list = mapper.findAllByGroup(groupId);
		Map<String, String> map = new HashMap<>();
		for (Message message : list) {
			map.put(message.getMessage(), message.getAnswer());
		}
		return map;
	}

	/**
	 * 根据消息内容查询对应的回复内容
	 * 
	 * @param groupId 群号
	 * @param message 消息内容
	 * @return 对应的回复内容
	 */
	@Override
	public String getAnswerByMessage(String groupId, String message) {
		return mapper.findAnswerByMessage(groupId, message);
	}

	/**
	 * 更改自动回复消息
	 * 
	 * @param groupId 群号
	 * @param message 消息内容
	 * @param answer  修改之后的回复内容
	 * @return
	 */
	private int changeMessage(String groupId, String message, String answer) {
		removeMessage(groupId, message);
		addMessage(groupId, message, answer);
		return 2;
	}
}
