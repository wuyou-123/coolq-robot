package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.utils.CQ;

import java.util.Objects;

/**
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@Beans
public class MessageFilter {

	@DIYFilter("addMessage")
	public class addMessage implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			String regex = ".*添加消息.*回复.*";
			return message.matches(regex) && at.test()
					&& Objects.equals(CQ.getAt(message), msgget.getThisCode());
		}

	}

	@DIYFilter("removeMessage")
	public class removeMessage implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			String regex = ".*删除消息.*";
			return message.matches(regex) && at.test()
					&& Objects.equals(CQ.getAt(message), msgget.getThisCode());
		}

	}

}
