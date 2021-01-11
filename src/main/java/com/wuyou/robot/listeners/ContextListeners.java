package com.wuyou.robot.listeners;

//import love.forte.simbot.annotation.Listen;


import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.Listens;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;

import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class ContextListeners {

    @Listens(value = {@Listen(GroupMsg.class)}, priority = 10)
    @Filters(customFilter = "boot")
    public void getContext(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String message = msg.getMsg();
        if (CQ.CONTEXT.get(fromGroup) == null) {
            CQ.CONTEXT.put(fromGroup, new String[]{"", "", "", "", message});
            return;
        }
        String[] ret = SenderUtil.getList(fromGroup);

        try {
            if (ret[2].equals(message) && ret[3].equals(message) && ret[4].equals(message)) {
                int ran = new Random().nextInt(6) + 1;
                if (ran <= 4) {
                    SenderUtil.sendGroupMsg(fromGroup, message);
                } else if (ran == 5) {
                    SenderUtil.sendGroupMsg(fromGroup, "打断复读~~~");
                } else {
                    SenderUtil.sendGroupMsg(fromGroup, "打断打断!!!");
                }
                ret = new String[]{"", "", "", "", message};
            }
            CQ.CONTEXT.put(fromGroup, new String[]{ret[1], ret[2], ret[3], ret[4], message});
///            System.out.println(Arrays.toString(CQ.CONTEXT.get(fromGroup)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
