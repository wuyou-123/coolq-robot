package com.wuyou.robot.filter;

import com.wuyou.utils.CQ;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 判断是否是开/关机指令
 *
 * @author Administrator<br>
 * 2020年5月2日
 */
@Component
public class GroupBootFilter {
    @Component("groupBoot")
    public static class Boot implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                GroupMsg msg = (GroupMsg) msgget;
                String str = msg.getMsg();
                if ("开机".equals(str.trim())) {
                    return true;
                }
                str = CQ.UTILS.remove(str);
                return "开机".equals(str.trim()) && data.getAtDetection().atBot();
            }
            return false;
        }
    }

    @Component("groupShutDown")
    public static class ShutDown implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                GroupMsg msg = (GroupMsg) msgget;
                String str = msg.getMsg();
                if ("关机".equals(str.trim())) {
                    return true;
                }
                str = CQ.UTILS.remove(str);
                return "关机".equals(str.trim()) && data.getAtDetection().atBot();
            }
            return false;
        }
    }
}