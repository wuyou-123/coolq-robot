package com.wuyou.service;

import java.util.Map;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface StatService {
    /**
     * 设置开关机状态
     *
     * @param groupId 群号
     * @param stat    状态
     */
    void bootAndShutDown(String groupId, int stat);

    /**
     * 查询状态
     *
     * @param groupId 群号
     * @return 开关机状态
     */
    int getStat(String groupId);

    /**
     * 获取所有群开关机状态
     *
     * @return 状态列表
     */
    Map<String, Boolean> getAllStat();
}
