package com.wuyou.jobs;

import com.forte.qqrobot.anno.depend.Depend;
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
//@CronTask("0 0 0 * * ? ")
//@CronTask("0/5 * * * * ? ")
//@Beans
public class TimesJob2 implements TimeJob {

	@Depend
	StatService statService;

	@Depend
	ClearService clearService;

	@Override
	public void execute(MsgSender msg, CQCodeUtil CQCodeUtil) {
		System.out.println("啊啊啊");
	}

}
