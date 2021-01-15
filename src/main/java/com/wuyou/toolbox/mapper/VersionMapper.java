package com.wuyou.toolbox.mapper;

import com.wuyou.toolbox.entity.Version;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wuyou
 */
@Repository
public interface VersionMapper {

    /**
     * 获取版本
     *
     * @param versionId 版本号
     * @return 版本
     */
    Version findVersion(String versionId);

    /**
     * 获取最新版本
     *
     * @return 最新的版本
     */
    Version findMaxVersion();

    /**
     * 获取所有版本
     *
     * @return 版本列表
     */
    List<Version> findAllVersion();

    /**
     * 添加一个版本
     *
     * @param version 添加的版本实体
     * @return 插入的数量
     */
    Integer saveVersion(Version version);
}
