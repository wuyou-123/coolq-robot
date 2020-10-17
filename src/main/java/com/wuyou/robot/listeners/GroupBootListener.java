package com.wuyou.robot.listeners;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.wuyou.service.StatService;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;

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

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "groupBoot")
    public void sendBoot(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        if (PowerUtils.getPowerType(fromGroup, fromQQ, sender) > 1) {
            service.bootAndShutDown(msg.getGroup(), 1);
            GlobalVariable.bootMap.put(msg.getGroup(), true);
            SenderUtil.sendGroupMsg(sender, msg.getGroup(), "已开机, 发送\"菜单\"查看帮助信息, 艾特我可以和我聊天哦");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "groupShutDown")
    public void sendShutDown(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        if (PowerUtils.getPowerType(fromGroup, fromQQ, sender) > 1) {
            service.bootAndShutDown(msg.getGroup(), 0);
            GlobalVariable.bootMap.put(msg.getGroup(), false);
            SenderUtil.sendGroupMsg(sender, msg.getGroup(), "已关机");
        }
    }

}
