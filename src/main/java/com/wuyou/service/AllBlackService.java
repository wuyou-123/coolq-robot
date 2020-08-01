package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface AllBlackService {
	void addAllBlack(int type, String user);

	void removeAllBlack(int type, String user);

	List<String> getAllBlack(int type);
}
