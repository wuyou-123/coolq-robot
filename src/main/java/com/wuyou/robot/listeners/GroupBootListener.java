package com.wuyou.robot.listeners;

import com.wuyou.service.StatService;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;

/**
 * 控制机器人开关机的类
 *
 * @author Administrator<br>
 * 2020年5月2日
 */
@Beans
public class GroupBootListener {
    @Depend
    StatService service;

    @OnGroup
    @Filters(customFilter = "groupBoot")
    public void sendBoot(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String fromQQ = msg.getAccountInfo().getAccountCode();
        if (PowerUtils.getPermissions(fromGroup, fromQQ, sender) > 1) {
            service.bootAndShutDown(msg.getGroupInfo().getGroupCode(), 1);
            GlobalVariable.BOOT_MAP.put(msg.getGroupInfo().getGroupCode(), true);
            SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), "已开机, 发送\"菜单\"查看帮助信息, 艾特我可以和我聊天哦");
        }
    }

    @OnGroup
    @Filters(customFilter = "groupShutDown")
    public void sendShutDown(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String fromQQ = msg.getAccountInfo().getAccountCode();
        if (PowerUtils.getPermissions(fromGroup, fromQQ, sender) > 1) {
            service.bootAndShutDown(msg.getGroupInfo().getGroupCode(), 0);
            GlobalVariable.BOOT_MAP.put(msg.getGroupInfo().getGroupCode(), false);
            SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), "已关机");
        }
    }

}
