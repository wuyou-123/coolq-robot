package com.wuyou.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.anno.timetask.CronTask;
import com.forte.qqrobot.beans.messages.result.GroupList;
import com.forte.qqrobot.beans.messages.result.inner.Group;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.timetask.TimeJob;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.wuyou.service.ClearService;
import com.wuyou.service.StatService;

/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
@CronTask("0 0 0 * * ? ")
//@CronTask("0/5 * * * * ? ")
@Beans
public class TimesJob implements TimeJob {

	@Depend
	StatService statService;

	@Depend
	ClearService clearService;

	@Override
	public void execute(MsgSender msg, CQCodeUtil CQCodeUtil) {
		System.out.println("删除无用群信息");
		GroupList groupList = msg.GETTER.getGroupList();
		Set<String> set = statService.getAllStat().keySet();
		List<String> list = new ArrayList<String>();
		for (Group group : groupList) {
			list.add(group.getCode());
		}
		for (String string : set) {
			if (!list.contains(string)) {
				clearService.clearAllData(string);
			}
		}

	}

}