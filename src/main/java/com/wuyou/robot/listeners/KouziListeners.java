package com.wuyou.robot.listeners;

import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;

import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class KouziListeners {

    //	@Listen(MsgGetTypes.groupMsg)
    public void kouzi(GroupMsg msg, MsgSender sender) {
        if (msg.getGroup().equals("475451692")) {
            String kouzi = GlobalVariable.kouzi;
            String[] kou = kouzi.split("\n");
            Random r = new Random();
            for (int i = 0; i < 100; i++) {
                int num = r.nextInt(kou.length);
                SenderUtil.sendGroupMsg(sender, msg.getGroup(), kou[num]);
            }
        }
    }
}
