package com.wuyou.service;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface AllBlackService {

    /**
     * 添加全局黑名单
     *
     * @param type 类型
     * @param user QQ号
     */
    void addAllBlack(int type, String user);

    /**
     * 删除全局黑名单
     *
     * @param type 类型
     * @param user QQ号
     */
    void removeAllBlack(int type, String user);

    /**
     * 获取全局黑名单
     *
     * @param type 类型
     * @return 查询到的QQ号
     */
    List<String> getAllBlack(int type);
}
