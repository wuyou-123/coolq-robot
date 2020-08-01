package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.service.MessageService;
import com.wuyou.utils.CQ;

/**
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@Beans
public class AiFilter {
	@Depend
	MessageService service;

	@DIYFilter("ai")
	public class AiMessage implements Filterable {
		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			String atThis = CQ.at(msgget.getThisCode());
			String regex1 = ".*添加消息.*回复.*";
			String regex2 = ".*(删除关键词|添加关键词|删除消息|百度|菜单|功能|色图|来点涩图|来点色图|艾特全体).*";
			if ((message.matches(regex1) || message.matches(regex2) || message.replace(atThis, "").trim().equals("自闭")
					|| message.replace(atThis, "").trim().equals("开机")
					|| message.replace(atThis, "").trim().equals("关机")
					|| message.replace(atThis, "").trim().startsWith("领取套餐")) && at.test()
					&& message.startsWith(CQ.at(msgget.getThisCode())))
				return false;
			if (at.test() && message.startsWith(atThis.trim()) && !message.replace(atThis, "").trim().startsWith("说")) {
				return true;
			}
			return false;
		}
	}

	@DIYFilter("aiVoice")
	public class AiVoice implements Filterable {
		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			String atThis = CQ.at(msgget.getThisCode());
			String regex1 = ".*添加消息.*回复.*";
			String regex2 = ".*(删除关键词|添加关键词|删除消息|百度).*";
			if ((message.matches(regex1) || message.matches(regex2) || message.replace(atThis, "").trim().equals("自闭")
					|| message.replace(atThis, "").trim().equals("开机")
					|| message.replace(atThis, "").trim().equals("关机")
					|| message.replace(atThis, "").trim().startsWith("领取套餐")) && at.test()
					&& message.startsWith(CQ.at(msgget.getThisCode())))
				return false;
			if (at.test() && message.startsWith(atThis.trim()) && message.replace(atThis, "").trim().startsWith("说")) {
				return true;
			}
			return false;
		}
	}
}
