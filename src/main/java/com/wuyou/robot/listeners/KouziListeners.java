package com.wuyou.robot.listeners;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;

import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class KouziListeners {

    @OnGroup
    public void kouzi(GroupMsg msg, MsgSender sender) {
        if ("475451692".equals(msg.getGroupInfo().getGroupCode())) {
            String kouzi = GlobalVariable.KOUZI.toString();
            String[] kou = kouzi.split("\n");
            Random r = new Random();
            for (int i = 0; i < 100; i++) {
                int num = r.nextInt(kou.length);
                SenderUtil.sendGroupMsg(sender, msg.getGroupInfo().getGroupCode(), kou[num]);
            }
        }
    }
}
