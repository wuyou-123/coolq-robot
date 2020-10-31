package com.wuyou.service.impl;

import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.exception.UpdateException;
import com.wuyou.mapper.BlackUserMapper;
import com.wuyou.service.BlackUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 黑名单控制器
 * 
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Service
public class BlackUserServiceImpl implements BlackUserService {

	private final BlackUserMapper mapper;

	@Autowired
	public BlackUserServiceImpl(BlackUserMapper mapper) {
		this.mapper = mapper;
	}
	/**
	 * 添加黑名单
	 * 
	 * @param groupId 群号
	 * @param user    用户QQ号
	 */
	@Override
	public void addBlackUser(String groupId, String user) {
		List<String> list = getUserByGroupId(groupId);
		if (!list.contains(user)) {
			int row = mapper.insertBlackUser(groupId, user);
			if (row != 1) {
				throw new UpdateException("插入数据时出现异常,请联系管理员");
			}
		} else {
			throw new ObjectExistedException("此用户已存在");
		}
	}

	/**
	 * 删除黑名单
	 * 
	 * @param groupId 群号
	 * @param user    黑名单用户QQ号
	 */
	@Override
	public void removeBlackUser(String groupId, String user) {
		int row = mapper.deleteBlackUser(groupId, user);
		if (row == 0) {
			throw new ObjectNotFoundException("未找到此用户");
		} else if (row != 1) {
			throw new UpdateException("更新数据时出现异常,请联系管理员");
		}
	}

	/**
	 * 查询所有黑名单用户
	 * 
	 * @param groupId 群号
	 * @return 黑名单用户集合
	 */
	@Override
	public List<String> getUserByGroupId(String groupId) {
		return mapper.findUserByGroupId(groupId);
	}
}
