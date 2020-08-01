package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface ManagerService {
	void addManager(String groupId, String user);

	void removeManager(String groupId, String user);

	List<String> getManagerByGroupId(String groupId);
}
