package com.wuyou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wuyou.entity.Message;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface MessageMapper {
	Integer insertMessage(@Param("groupId") String groupId, @Param("message") String message,
			@Param("answer") String answer);

	Integer deleteMessage(@Param("groupId") String groupId, @Param("message") String message);

	List<Message> findAllByGroup(String groupId);

	String findAnswerByMessage(@Param("groupId") String groupId, @Param("message") String message);
}
