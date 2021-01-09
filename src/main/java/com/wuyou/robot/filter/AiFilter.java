package com.wuyou.robot.filter;

import com.wuyou.service.MessageService;
import com.wuyou.utils.CQ;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * AI相关过滤器
 *
 * @author wuyou
 */
@Component
public class AiFilter {
    @Depend
    MessageService service;

    @Component("ai")
    public static class AiMessage implements ListenerFilter {
        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                final String msg = CQ.UTILS.remove(message, true, true);
                String regex1 = ".*添加消息.*回复.*";
                String regex2 = ".*(删除关键词|添加关键词|删除消息|百度|菜单|功能|色图|来点涩图|来点色图|艾特全体).*";
                if ((message.matches(regex1) || message.matches(regex2) || "自闭".equals(msg)
                        || "开机".equals(msg)
                        || "关机".equals(msg)
                        || msg.startsWith("领取套餐"))) {
                    return false;
                }
                return data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode()) && !msg.startsWith("说");
            }
            return false;
        }
    }

    @Component("aiVoice")
    public static class AiVoice implements ListenerFilter {
        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                final String msg = CQ.UTILS.remove(message, true, true);
                String regex1 = ".*添加消息.*回复.*";
                String regex2 = ".*(删除关键词|添加关键词|删除消息|百度).*";
                if (message.matches(regex1) || message.matches(regex2) || "自闭".equals(msg)
                        || "开机".equals(msg)
                        || "关机".equals(msg)
                        || msg.startsWith("领取套餐")) {
                    return false;
                }
                return data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode()) && msg.startsWith("说");
            }
            return false;
        }
    }
}
