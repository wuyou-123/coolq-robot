package com.wuyou.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.exception.UpdateException;
import com.wuyou.mapper.AllBlackMapper;
import com.wuyou.service.AllBlackService;

/**
 * 全局黑名单控制器
 * 
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Service
public class AllBlackServiceImpl implements AllBlackService {
	@Autowired
	AllBlackMapper mapper;

	/**
	 * 添加全局黑名单
	 * 
	 * @param user 用户QQ号
	 * @return
	 */
	@Override
	public void addAllBlack(int type, String user) {
		List<String> list = getAllBlack(type);
		if (!list.contains(user)) {
			int row = mapper.insertAllBlack(type, user);
			if (row != 1) {
				throw new UpdateException("插入数据时出现异常,请联系管理员");
			}
		} else {
			throw new ObjectExistedException("此用户已存在");
		}
	}

	/**
	 * 删除全局黑名单
	 * 
	 * @param user 黑名单用户QQ号
	 */
	@Override
	public void removeAllBlack(int type, String user) {
		int row = mapper.deleteAllBlack(type, user);
		if (row == 0) {
			throw new ObjectNotFoundException("未找到此用户");
		} else if (row != 1) {
			throw new UpdateException("更新数据时出现异常,请联系管理员");
		}
	}

	/**
	 * 查询所有全局黑名单用户
	 * 
	 * @return 全局黑名单用户集合
	 */
	@Override
	public List<String> getAllBlack(int type) {
		return mapper.findAllBlack(type);
	}
}
