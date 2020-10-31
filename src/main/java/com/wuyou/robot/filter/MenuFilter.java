package com.wuyou.robot.filter;

import java.util.ArrayList;
import java.util.List;

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
@DIYFilter("menu")
public class MenuFilter implements Filterable {

	@Override
	public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
		List<String> list = new ArrayList<>();
		list.add("菜单");
		list.add("功能");
		String message = msgget.getMsg();
		String msg = CQ.UTILS.remove(message, true, true);
		return list.contains(msg);
	}

}
