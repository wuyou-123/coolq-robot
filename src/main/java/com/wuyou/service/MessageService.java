package com.wuyou.service;

import java.util.Map;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
public interface MessageService {
    /**
     * 添加自动回复消息
     *
     * @param groupId 群号
     * @param message 消息内容
     * @param answer  回复内容
     * @return 添加成功的数量
     */
    Integer addMessage(String groupId, String message, String answer);


    /**
     * 删除自动回复消息
     *
     * @param groupId 群号
     * @param message 消息内容
     */
    void removeMessage(String groupId, String message);

    /**
     * 根据群号查询所有自动回复消息
     *
     * @param groupId 群号
     * @return 自动回复消息列表
     */
    Map<String, String> getAllByGroup(String groupId);

    /**
     * 根据消息内容查找回复内容
     *
     * @param groupId 群号
     * @param message 消息内容
     * @return 回复内容
     */
    String getAnswerByMessage(String groupId, String message);
}
