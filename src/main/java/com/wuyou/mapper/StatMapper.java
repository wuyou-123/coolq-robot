package com.wuyou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wuyou.entity.Stat;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
public interface StatMapper {
	void bootAndShutDown(@Param("groupId") String groupId, @Param("stat") int stat);

	Integer findStat(String groupId);

	void changeStat(@Param("groupId") String groupId, @Param("stat") int stat);

	List<Stat> findAllStat();
}
