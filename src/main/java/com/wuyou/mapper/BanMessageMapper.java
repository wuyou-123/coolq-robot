package com.wuyou.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface BanMessageMapper {
    /**
     * 添加禁言关键词
     *
     * @param groupId 群号
     * @param message 消息
     * @return 添加的数量
     */
    Integer insertBanMessage(@Param("groupId") String groupId, @Param("message") String message);

    /**
     * 删除禁言关键词
     *
     * @param groupId 群号
     * @param message 消息
     * @return 删除的数量
     */
    Integer deleteBanMessage(@Param("groupId") String groupId, @Param("message") String message);

    /**
     * 查询禁言关键词
     *
     * @param groupId 群号
     * @return 查询到的QQ号
     */
    List<String> findAllByGroupId(String groupId);
}
