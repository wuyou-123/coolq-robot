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
public class AutisticFilter {

    @Component("autistic1")
    public static class Autistic1 implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg();
                if ("自闭".equals(message)) {
                    return true;
                }
                final String msg = CQ.UTILS.remove(message, true, true);
                return data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode()) && "自闭".equals(msg);
            }
            return false;
        }
    }

    @Component("autistic2")
    public static class Autistic2 implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg();
                if (message.startsWith("领取套餐")) {
                    return true;
                }
                final String msg = CQ.UTILS.remove(message, true, true);
                return data.getAtDetection().atBot() && Objects.equals(CQ.startsWithAt(message), msgget.getBotInfo().getBotCode()) && msg.startsWith("领取套餐");
            }
            return false;
        }
    }
}
