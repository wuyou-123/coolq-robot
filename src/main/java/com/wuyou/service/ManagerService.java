package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface ManagerService {
    /**
     * 添加管理员
     *
     * @param groupId 群号
     * @param user    QQ号
     */
    void addManager(String groupId, String user);

    /**
     * 删除管理员
     *
     * @param groupId 群号
     * @param user    QQ号
     */
    void removeManager(String groupId, String user);

    /**
     * 获取管理员
     *
     * @param groupId 群号
     * @return 查询到的QQ号
     */
    List<String> getManagerByGroupId(String groupId);
}
