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

import java.util.Objects;

/**
 * AI相关过滤器
 *
 * @author wuyou
 */
@Beans
public class AiFilter {
    @Depend
    MessageService service;

    @DIYFilter("ai")
    public static class AiMessage implements Filterable {
        @Override
        public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
            String message = msgget.getMsg().trim();
            final String msg = CQ.UTILS.remove(message, true, true);
            String regex1 = ".*添加消息.*回复.*";
            String regex2 = ".*(删除关键词|添加关键词|删除消息|百度|菜单|功能|色图|来点涩图|来点色图|艾特全体).*";
            if ((message.matches(regex1) || message.matches(regex2) || "自闭".equals(msg)
                    || "开机".equals(msg)
                    || "关机".equals(msg)
                    || msg.startsWith("领取套餐"))) {
                return false;
            }
            return at.test() && Objects.equals(CQ.getAt(message), msgget.getThisCode()) && message.startsWith("[") && !msg.startsWith("说");
        }
    }

    @DIYFilter("aiVoice")
    public static class AiVoice implements Filterable {
        @Override
        public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
            String message = msgget.getMsg();
            final String msg = CQ.UTILS.remove(message, true, true);
            String regex1 = ".*添加消息.*回复.*";
            String regex2 = ".*(删除关键词|添加关键词|删除消息|百度).*";
            if (message.matches(regex1) || message.matches(regex2) || "自闭".equals(msg)
                    || "开机".equals(msg)
                    || "关机".equals(msg)
                    || msg.startsWith("领取套餐")) {
                return false;
            }
            return at.test() && Objects.equals(CQ.getAt(message), msgget.getThisCode()) && message.startsWith("[") && msg.startsWith("说");
        }
    }
}
