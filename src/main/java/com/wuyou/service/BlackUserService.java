package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface BlackUserService {
    /**
     * 添加黑名单
     *
     * @param groupId 群号
     * @param user    QQ号
     */
    void addBlackUser(String groupId, String user);

    /**
     * 删除黑名单
     *
     * @param groupId 群号
     * @param user    QQ号
     */
    void removeBlackUser(String groupId, String user);

    /**
     * 获取黑名单
     *
     * @param groupId 群号
     * @return 查询到的QQ号
     */
    List<String> getUserByGroupId(String groupId);
}
