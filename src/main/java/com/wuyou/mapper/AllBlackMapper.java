package com.wuyou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface AllBlackMapper {
	Integer insertAllBlack(@Param("type") int type, @Param("user") String user);

	Integer deleteAllBlack(@Param("type") int type, @Param("user") String user);

	List<String> findAllBlack(int type);
}
