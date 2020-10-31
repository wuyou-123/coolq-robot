package com.wuyou.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator<br>
 * 2020年5月11日
 */
@Repository
public interface AllBlackMapper {
    /**
     * 添加全局黑名单
     *
     * @param type 类型
     * @param user QQ号
     * @return 成功的数量
     */
    Integer insertAllBlack(@Param("type") int type, @Param("user") String user);

    /**
     * 删除全局黑名单
     *
     * @param type 类型
     * @param user QQ号
     * @return 删除的数量
     */
    Integer deleteAllBlack(@Param("type") int type, @Param("user") String user);

    /**
     * 获取全局黑名单
     *
     * @param type 类型
     * @return 查询到的QQ号
     */
    List<String> findAllBlack(int type);
}
