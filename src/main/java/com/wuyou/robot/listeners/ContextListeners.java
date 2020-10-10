package com.wuyou.robot.listeners;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class ContextListeners {

    @Listen(value = MsgGetTypes.groupMsg, sort = 10)
    @Filter(diyFilter = "boot")
    public void getContext(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String message = msg.getMsg();
        if (CQ.context.get(fromGroup) == null) {
            CQ.context.put(fromGroup, new String[]{"", "", "", "", message});
            return;
        }
        String[] ret = SenderUtil.getList(fromGroup);

        try {
            if (ret[2].equals(message) && ret[3].equals(message) && ret[4].equals(message)) {
                int ran = new Random().nextInt(6) + 1;
                if (ran <= 4)
                    SenderUtil.sendGroupMsg(sender, fromGroup, message);
                else if (ran == 5)
                    SenderUtil.sendGroupMsg(sender, fromGroup, "打断复读~~~");
                else SenderUtil.sendGroupMsg(sender, fromGroup, "打断打断!!!");
                ret = new String[]{"", "", "", "", message};
            }
            CQ.context.put(fromGroup, new String[]{ret[1], ret[2], ret[3], ret[4], message});
//            System.out.println(Arrays.toString(CQ.context.get(fromGroup)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
