package com.wuyou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface ManagerMapper {
	Integer insertManager(@Param("groupId")String groupId, @Param("user")String user);

	Integer deleteManager(@Param("groupId")String groupId, @Param("user")String user);

	List<String> findManagerByGroupId(String groupId);
}
