package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface BanMessageService {
	void addBanMessage(String groupId, String message);

	void removeBanMessage(String groupId, String message);

	List<String> getAllByGroupId(String groupId);
}
