package com.wuyou.service;

import java.util.Map;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface MessageService {
	Integer addMessage(String groupId, String message, String answer);

	void removeMessage(String groupId, String message);

	Map<String, String> getAllByGroup(String groupId);

	String getAnswerByMessage(String groupId, String message);
}
