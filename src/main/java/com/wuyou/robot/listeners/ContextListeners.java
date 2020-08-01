package com.wuyou.robot.listeners;

import java.util.Random;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.wuyou.utils.CQ;

/**
 * @author Administrator<br>
 *         2020年5月3日
 *
 */
@Beans
public class ContextListeners {

	@Listen(MsgGetTypes.groupMsg)
	@Filter(diyFilter = "boot")
	public void getContext(GroupMsg msg, MsgSender sender) {
		String fromGroup = msg.getGroup();
		String message = msg.getMsg();
		if (CQ.context.get(fromGroup) == null) {
			CQ.context.put(fromGroup, new String[] { "", "", "", message });
			return;
		}
		String first = CQ.context.get(fromGroup)[0];
		String second = CQ.context.get(fromGroup)[1];
		String third = CQ.context.get(fromGroup)[2];
		String fourth = CQ.context.get(fromGroup)[3];
		String[] ret = new String[] { second, third, fourth, message };
		try {
			if (first.equals(second) && second.equals(third) && third.equals(fourth) && fourth.equals(message)) {
				int ran = new Random().nextInt(6) + 1;
				if (ran <= 4)
					sender.SENDER.sendGroupMsg(fromGroup, message);
				else if (ran == 5)
					sender.SENDER.sendGroupMsg(fromGroup, "打断复读~~~");
				else if (ran == 6)
					sender.SENDER.sendGroupMsg(fromGroup, "打断打断!!!");
				ret = new String[] { "", "", "", message };
			}
			CQ.context.put(fromGroup, ret);
		} catch (Exception e) {
		}
	}
}
