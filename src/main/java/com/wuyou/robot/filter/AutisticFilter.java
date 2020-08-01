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
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@Beans
public class AutisticFilter {

	@DIYFilter("autistic1")
	public class Autistic1 implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			if (message.equals("自闭")) {
				return true;
			}
			String atThis = CQ.at(msgget.getThisCode());
			if (at.test() && message.startsWith(atThis) && message.replace(atThis, "").trim().equals("自闭")) {
				return true;
			}
			return false;
		}
	}

	@DIYFilter("autistic2")
	public class Autistic2 implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			if (message.startsWith("领取套餐")) {
				return true;
			}
			String atThis = CQ.at(msgget.getThisCode());
			if (at.test() && message.startsWith(atThis) && message.replace(atThis, "").trim().startsWith("领取套餐")) {
				return true;
			}
			return false;
		}

	}
}
