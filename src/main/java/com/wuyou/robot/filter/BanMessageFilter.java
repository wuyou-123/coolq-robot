package com.wuyou.robot.filter;

import com.wuyou.utils.CQ;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Administrator<br>
 * 2020年5月2日
 */
@Component
public class BanMessageFilter {

    @Component("addBanMessage")
    public static class AddBanMessage implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                String regex = ".*添加关键词.*";
                return message.matches(regex) && data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode());
            }
            return false;
        }

    }

    @Component("removeBanMessage")
    public static class RemoveBanMessage implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                String regex = ".*删除关键词.*";
                return message.matches(regex) && data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode());
            }
            return false;
        }
    }

    @Component("sendBanMessage")
    public static class SendBanMessage implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                String regex = ".*(删除关键词|添加关键词).*";
                return !(message.matches(regex) && data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode()));
            }
            return false;
        }
    }
}
