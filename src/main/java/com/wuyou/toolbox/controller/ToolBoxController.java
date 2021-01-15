package com.wuyou.toolbox.controller;

import com.wuyou.toolbox.common.RestCode;
import com.wuyou.toolbox.common.RestResponse;
import com.wuyou.toolbox.entity.Version;
import com.wuyou.toolbox.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuyou
 */
@RestController
@RequestMapping("toolbox")
public class ToolBoxController {
    @Autowired
    VersionService service;

    @RequestMapping("test")
    public RestResponse<String> test() {
        return RestResponse.success();
    }

    @RequestMapping("getUpdate")
    public RestResponse<Version> getUpdate(String versionId) {
        Version ver = service.getVersion(versionId);
        Version latest = service.getMaxVersion();
        if (ver.getId() == latest.getId()) {
            return RestResponse.success();
        }
        return RestResponse.success(latest);
    }

    @PostMapping("newVersion")
    public RestResponse<Version> newVersion(Version version) {
        if (service.getVersion(version.getVersionId()) != null) {
            return RestResponse.error(RestCode.VERSION_ALREADY_EXISTS);
        }
        if (service.saveVersion(version) == 1) {
            return RestResponse.success();
        }
        return RestResponse.error(RestCode.ERROR);

    }
}
