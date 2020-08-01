package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface BlackUserService {
	void addBlackUser(String groupId, String user);

	void removeBlackUser(String groupId, String user);

	List<String> getUserByGroupId(String groupId);
}
