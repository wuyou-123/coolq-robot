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
 * 获取现在机器人是否开机
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@Beans
public class GroupManagementFilter {

	@DIYFilter("kickMember")
	public class KickMember implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("踢") && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("banMember")
	public class BanMember implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("禁言") && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("cancelBanMember")
	public class CancelBanMember implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("解禁") && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("blackMember")
	public class BlackMember implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("拉黑")) {
				if (CQ.getAts(msg).size() > 0) {
					return true;
				}
				String[] users = msg.substring(msg.indexOf("拉黑") + 2).split(",");
				for (String user1 : users) {
					for (String user2 : user1.split("，")) {
						try {
							Long.parseLong(user2);
							return true;
						} catch (Exception e) {
							return false;
						}
					}
				}
			}
			return false;
		}

	}

	@DIYFilter("cancelBlackMember")
	public class CancelBlackMember implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("取消拉黑")) {
				if (CQ.getAts(msg).size() > 0) {
					return true;
				}
				String[] users = msg.substring(msg.indexOf("取消拉黑") + 4).split(",");
				for (String user1 : users) {
					for (String user2 : user1.split("，")) {
						try {
							Long.parseLong(user2);
							return true;
						} catch (Exception e) {
							return false;
						}
					}
				}
			}
			return false;
		}

	}

	@DIYFilter("changeNick")
	public class ChangeNick implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("改名") && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("changeTitle")
	public class ChangeTitle implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if (msg.startsWith("给头衔") && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("addManager")
	public class AddManager implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();

			if ((msg.startsWith("添加管理") || msg.startsWith("设置管理") || msg.startsWith("给管理")) && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("removeManager")
	public class RemoveManager implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if ((msg.startsWith("删除管理") || msg.startsWith("取消管理") || msg.startsWith("移除管理")) && CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("addGroupManager")
	public class AddGroupManager implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();

			if ((msg.startsWith("添加群管理") || msg.startsWith("设置群管理") || msg.startsWith("给群管理"))
					&& CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

	@DIYFilter("removeGroupManager")
	public class RemoveGroupManager implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String msg = msgget.getMsg();
			if ((msg.startsWith("删除群管理") || msg.startsWith("取消群管理") || msg.startsWith("移除群管理"))
					&& CQ.getAt(msg) != null) {
				return true;
			}
			return false;
		}

	}

}
