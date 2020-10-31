package com.wuyou.mapper;

import org.springframework.stereotype.Repository;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface ClearMapper {
    /**
     * 删除消息
     *
     * @param groupId 群号
     */
    void clearMessage(String groupId);

    /**
     * 删除禁言关键词
     *
     * @param groupId 群号
     */
    void clearBanMessage(String groupId);

    /**
     * 删除黑名单
     *
     * @param groupId 群号
     */
    void clearBlackUser(String groupId);

    /**
     * 删除管理员
     *
     * @param groupId 群号
     */
    void clearManager(String groupId);

    /**
     * 删除开关机状态
     *
     * @param groupId 群号
     */
    void clearStat(String groupId);
}
