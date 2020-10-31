package com.wuyou.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface ManagerMapper {
    /**
     * 添加管理员
     *
     * @param groupId 群号
     * @param user    QQ号
     * @return 插入的数量
     */
    Integer insertManager(@Param("groupId") String groupId, @Param("user") String user);

    /**
     * 删除管理员
     *
     * @param groupId 群号
     * @param user    QQ号
     * @return 删除的数量
     */
    Integer deleteManager(@Param("groupId") String groupId, @Param("user") String user);

    /**
     * 获取管理员
     *
     * @param groupId 群号
     * @return 查询到的QQ号
     */
    List<String> findManagerByGroupId(String groupId);
}
