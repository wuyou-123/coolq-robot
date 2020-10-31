package com.wuyou.service;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface ClearService {
    /**
     * 删除群所有数据
     *
     * @param groupId 群号
     */
    void clearAllData(String groupId);
}
