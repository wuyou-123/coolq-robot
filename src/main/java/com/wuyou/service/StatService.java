package com.wuyou.service;

import java.util.Map;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface StatService {
	void bootAndShutDown(String groupId, int stat);

	int getStat(String groupId);

	Map<String, Boolean> getAllStat();
}
