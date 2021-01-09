package com.wuyou.jobs;

import com.wuyou.service.ClearService;
import com.wuyou.service.StatService;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.results.GroupList;
import love.forte.simbot.api.message.results.SimpleGroupInfo;
import love.forte.simbot.bot.BotManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

///@CronTask("0/5 * * * * ? ")
/**
 * @author Administrator<br>
 *         2020年5月11日
 *
 */
@Beans
@Configuration
@EnableScheduling
public class TimesJob {

	@Depend
	StatService statService;
	@Depend
	ClearService clearService;
	@Depend
	private BotManager botManager;

	@Scheduled(cron = "0 0 0 * * ? ")
	public void execute() {
		System.out.println("删除无用群信息");
		GroupList groupList = botManager.getDefaultBot().getSender().GETTER.getGroupList();
		Set<String> set = statService.getAllStat().keySet();
		List<String> list = new ArrayList<>();
		for (SimpleGroupInfo group : groupList) {
			list.add(group.getGroupCode());
		}
		for (String string : set) {
			if (!list.contains(string)) {
				clearService.clearAllData(string);
			}
		}
//		BotManager botManager = BotRuntime.getRuntime().getBotManager();
//		botManager.defaultBot().getSender().SENDER.sendGroupMsg();
	}

}
