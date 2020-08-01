package com.wuyou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface BanMessageMapper {
	Integer insertBanMessage(@Param("groupId")String groupId, @Param("message")String message);

	Integer deleteBanMessage(@Param("groupId")String groupId, @Param("message")String message);

	List<String> findAllByGroupId(String groupId);
}
