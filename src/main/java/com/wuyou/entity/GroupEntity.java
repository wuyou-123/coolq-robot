package com.wuyou.entity;

import com.forte.qqrobot.beans.messages.types.PowerType;
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
    PowerType role;
    /**
     * 群主信息
     */
    GroupMemberEntity owner;

}
