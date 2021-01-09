package com.wuyou.entity;

import love.forte.simbot.api.message.assists.Permissions;
import lombok.Data;

/**
 * 群实体类
 *
 * @author wuyou
 */
@Data
public class GroupEntity {
    /**
     * 群号
     */
    Long uin;
    /**
     * 群名称
     */
    String groupName;
    /**
     * 本人在群里的权限
     */
    Permissions role;
    /**
     * 群主信息
     */
    GroupMemberEntity owner;

}
