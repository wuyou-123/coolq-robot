package com.wuyou.mapper;

import com.wuyou.entity.Stat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface StatMapper {
    /**
     * 设置开关机状态
     *
     * @param groupId 群号
     * @param stat    状态
     */
    void bootAndShutDown(@Param("groupId") String groupId, @Param("stat") int stat);

    /**
     * 查询状态
     *
     * @param groupId 群号
     * @return 开关机状态
     */
    Integer findStat(String groupId);

    /**
     * 改变开关机状态
     *
     * @param groupId 群号
     * @param stat    状态
     */
    void changeStat(@Param("groupId") String groupId, @Param("stat") int stat);

    /**
     * 获取所有群开关机状态
     *
     * @return 状态列表
     */
    List<Stat> findAllStat();
}
