package com.wuyou.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.exception.UpdateException;
import com.wuyou.mapper.ManagerMapper;
import com.wuyou.service.ManagerService;

/**
 * 机器人管理员控制器
 * @author Administrator<br>2020年4月29日
 *
 */
@Service
public class ManagerServiceImpl implements ManagerService {
	@Autowired
	ManagerMapper mapper;

	/**
	 * 添加管理员
	 * 
	 * @param groupId 群号
	 * @param user    用户QQ号
	 */
	@Override
	public void addManager(String groupId, String user) {
		List<String> list = getManagerByGroupId(groupId);
		if (!list.contains(user)) {
			int row = mapper.insertManager(groupId, user);
			if (row != 1) {
				throw new UpdateException("插入数据时出现异常,请联系管理员");
			}
		} else {
			throw new ObjectExistedException("此用户已存在");
		}
	}

	/**
	 * 删除管理员
	 * 
	 * @param groupId 群号
	 * @param user   管理员用户QQ号
	 */
	@Override
	public void removeManager(String groupId, String user) {
		int row = mapper.deleteManager(groupId, user);
		if (row == 0) {
			throw new ObjectNotFoundException("未找到此用户");
		} else if (row != 1) {
			throw new UpdateException("更新数据时出现异常,请联系管理员");
		}
	}

	/**
	 * 查询所有管理员
	 * 
	 * @param groupId 群号
	 * @return 管理员用户集合
	 */
	@Override
	public List<String> getManagerByGroupId(String groupId) {
		return mapper.findManagerByGroupId(groupId);
	}
}
