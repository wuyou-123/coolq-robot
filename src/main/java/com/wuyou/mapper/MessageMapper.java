package com.wuyou.mapper;

import com.wuyou.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface MessageMapper {
    /**
     * 添加自动回复消息
     *
     * @param groupId 群号
     * @param message 消息内容
     * @param answer  回复内容
     * @return 添加成功的数量
     */
    Integer insertMessage(@Param("groupId") String groupId, @Param("message") String message,
                          @Param("answer") String answer);

    /**
     * 删除自动回复消息
     *
     * @param groupId 群号
     * @param message 消息内容
     * @return 删除的数量
     */
    Integer deleteMessage(@Param("groupId") String groupId, @Param("message") String message);

    /**
     * 根据群号查询所有自动回复消息
     *
     * @param groupId 群号
     * @return 自动回复消息列表
     */
    List<Message> findAllByGroup(String groupId);

    /**
     * 根据消息内容查找回复内容
     *
     * @param groupId 群号
     * @param message 消息内容
     * @return 回复内容
     */
    String findAnswerByMessage(@Param("groupId") String groupId, @Param("message") String message);
}
