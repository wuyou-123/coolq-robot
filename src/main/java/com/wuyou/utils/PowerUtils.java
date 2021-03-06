package com.wuyou.utils;

import com.wuyou.service.ManagerService;
import love.forte.simbot.api.message.assists.Permissions;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator<br>
 * 2020年4月29日
 */
@Component
public class PowerUtils {
    private static ManagerService service;

    @Autowired
    public void setService(final ManagerService managerService) {
        setServiceStatic(managerService);
    }

    public static void setServiceStatic(final ManagerService managerService) {
        PowerUtils.service = managerService;
    }

    /**
     * 获取群成员权限
     *
     * @return 0:群员 1:管理员 2:机器人管理员 3:群主 4:主人
     */
    public static int getPermissions(String group, String user, MsgSender sender) {
        if (GlobalVariable.ADMINISTRATOR.contains(user)) {
            return 4;
        }
///        Permissions power = GroupUtils.getGroupMembers(sender, group, user).get(0).getRole();
        Permissions power = sender.GETTER.getMemberInfo(group, user).getPermission();
        if (power.isOwner()) {
            return 3;
        }
        if (service.getManagerByGroupId(group).contains(user)) {
            return 2;
        }
        if (power.isAdmin()) {
            return 1;
        }
        return 0;
    }

    /**
     * 机器人和群成员的权限比较
     *
     * @return true:机器人权限较大 false:群成员权限较大或二者权限一致
     */
    public static boolean powerCompare(GroupMsg msg, String user, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
///        Permissions userPower = GroupUtils.getGroupMembers(sender, group, user).get(0).getRole();
///        Permissions thisPower = GroupUtils.getGroupMembers(sender, group, msg.getBotInfo().getBotCode()).get(0).getRole();
        Permissions userPower = sender.GETTER.getMemberInfo(group, user).getPermission();
        Permissions thisPower = sender.GETTER.getMemberInfo(group, msg.getBotInfo().getBotCode()).getPermission();
        if (userPower.isMember()) {
            if (thisPower.isAdmin() || thisPower.isOwner()) {
                return true;
            }
        }
        return userPower.isAdmin() && thisPower.isOwner();
    }
}
