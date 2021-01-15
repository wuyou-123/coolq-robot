package com.wuyou.toolbox.service.impl;

import com.wuyou.toolbox.entity.Version;
import com.wuyou.toolbox.mapper.VersionMapper;
import com.wuyou.toolbox.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuyou
 */
@Service
public class VersionServiceImpl implements VersionService {

    private final VersionMapper mapper;

    @Autowired
    public VersionServiceImpl(VersionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Version getVersion(String versionId) {
        return mapper.findVersion(versionId);
    }

    @Override
    public Version getMaxVersion() {
        return mapper.findMaxVersion();
    }

    @Override
    public List<Version> getAllVersion() {
        return mapper.findAllVersion();
    }

    @Override
    public Integer saveVersion(Version version) {
        return mapper.saveVersion(version);
    }

}
