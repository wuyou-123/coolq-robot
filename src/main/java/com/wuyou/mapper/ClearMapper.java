package com.wuyou.mapper;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface ClearMapper {
	void clearMessage(String groupId);

	void clearBanMessage(String groupId);

	void clearBlackUser(String groupId);

	void clearManager(String groupId);

	void clearStat(String groupId);
}
