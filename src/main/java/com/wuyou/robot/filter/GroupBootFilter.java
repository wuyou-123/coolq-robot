package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.utils.CQ;

/**
 * 判断是否是开/关机指令
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@Beans
public class GroupBootFilter {
	@DIYFilter("groupBoot")
	public class Boot implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String str = msgget.getMsg();
			if ("开机".equals(str.trim()))
				return true;
			str = CQ.utils.remove(str);
			return "开机".equals(str.trim()) && at.test();
		}
	}

	@DIYFilter("groupShutDown")
	public class ShutDown implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String str = msgget.getMsg();
			if ("关机".equals(str.trim()))
				return true;
			str = CQ.utils.remove(str);
			return "关机".equals(str.trim()) && at.test();
		}

	}

}
