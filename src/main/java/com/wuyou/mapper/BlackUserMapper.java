package com.wuyou.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface BlackUserMapper {
    /**
     * 添加黑名单
     *
     * @param groupId 群号
     * @param user    QQ号
     * @return 插入的数量
     */
    Integer insertBlackUser(@Param("groupId") String groupId, @Param("user") String user);

    /**
     * 删除黑名单
     *
     * @param groupId 群号
     * @param user    QQ号
     * @return 删除的数量
     */
    Integer deleteBlackUser(@Param("groupId") String groupId, @Param("user") String user);

    /**
     * 获取黑名单
     *
     * @param groupId 群号
     * @return 查询到的QQ号
     */
    List<String> findUserByGroupId(String groupId);
}
