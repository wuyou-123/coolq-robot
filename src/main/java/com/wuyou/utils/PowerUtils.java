package com.wuyou.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.PowerType;
import com.forte.qqrobot.sender.MsgSender;
import com.wuyou.service.ManagerService;

/**
 * @author Administrator<br>
 *         2020年4月29日
 *
 */
@Component
public class PowerUtils {
	static ManagerService service;

	@Autowired
	public void setService(ManagerService service) {
		PowerUtils.service = service;
	}

	/**
	 * 获取群成员权限
	 * 
	 * @param group
	 * @param user
	 * @param sender
	 * @return 0:群员 1:管理员 2:机器人管理员 3:群主 4:主人
	 */
	public static int getPowerType(String group, String user, MsgSender sender) {
		List<String> administrator = new ArrayList<String>();
		administrator.add("1097810498");
		administrator.add("1041025733");
		administrator.add("2973617637");
		if (administrator.contains(user)) {
			return 4;
		}
		PowerType power = sender.GETTER.getGroupMemberInfo(group, user).getPowerType();
		if (power.isOwner()) {
			return 3;
		}
		if (service.getManagerByGroupId(group).contains(user)) {
			return 2;
		}
		if (power.isAdmin()) {
			return 1;
		}
		return 0;
	}

	/**
	 * 机器人和群成员的权限比较
	 * 
	 * @param group
	 * @param user
	 * @return true:机器人权限较大 false:群成员权限较大或二者权限一致
	 */
	public static boolean powerCompare(GroupMsg msg, String user, MsgSender sender) {
		String group = msg.getGroup();
		PowerType userPower = sender.GETTER.getGroupMemberInfo(group, user).getPowerType();
		PowerType thisPower = sender.GETTER.getGroupMemberInfo(group, msg.getThisCode()).getPowerType();
		if (userPower.isMember() && (thisPower.isAdmin() || thisPower.isOwner())) {
			return true;
		}
		if (userPower.isAdmin() && thisPower.isOwner()) {
			return true;
		}
		return false;
	}
}
