package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.utils.CQ;

/**
 * 获取现在机器人是否开机
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@DIYFilter("leave")
public class LeaveFilter implements Filterable {

	@Override
	public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
		String str = msgget.getMsg();
		str = CQ.UTILS.remove(str);
		return "退群".equals(str.trim()) && at.test();
	}

}
