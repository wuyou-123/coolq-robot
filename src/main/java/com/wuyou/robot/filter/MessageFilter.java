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
public class MessageFilter {

    @Component("addMessage")
    public static class AddMessage implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                String regex = ".*添加消息.*回复.*";
                return message.matches(regex) && data.getAtDetection().atBot()
                        && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode());
            }
            return false;
        }

    }

    @Component("removeMessage")
    public static class RemoveMessage implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                String regex = ".*删除消息.*";
                return message.matches(regex) && data.getAtDetection().atBot()
                        && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode());
            }
            return false;
        }
    }
}
