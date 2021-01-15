package com.wuyou.toolbox.service;

import com.wuyou.toolbox.entity.Version;

import java.util.List;

/**
 * @author wuyou
 */
public interface VersionService {

    /**
     * 获取版本
     *
     * @param versionId 版本号
     * @return 版本
     */
    Version getVersion(String versionId);
    /**
     * 获取最新版本
     *
     * @return 版本
     */
    Version getMaxVersion();

    /**
     * 获取所有版本
     *
     * @return 版本列表
     */
    List<Version> getAllVersion();

    /**
     * 添加一个版本
     *
     * @param version 添加的版本实体
     * @return 插入的数量
     */
    Integer saveVersion(Version version);
}
