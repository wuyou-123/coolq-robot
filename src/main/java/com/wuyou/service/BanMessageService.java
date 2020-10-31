package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface BanMessageService {

    /**
     * 添加禁言关键词
     *
     * @param groupId 群号
     * @param message 消息
     */
    void addBanMessage(String groupId, String message);

    /**
     * 删除禁言关键词
     *
     * @param groupId 群号
     * @param message 消息
     */
    void removeBanMessage(String groupId, String message);

    /**
     * 查询禁言关键词
     *
     * @param groupId 群号
     * @return 查询到的QQ号
     */
    List<String> getAllByGroupId(String groupId);
}
