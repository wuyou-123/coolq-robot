package com.wuyou.robot.filter;

import com.wuyou.utils.CQ;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 获取现在机器人是否开机
 *
 * @author Administrator<br>
 * 2020年5月2日
 */
@Component("leave")
public class LeaveFilter implements ListenerFilter {

    @Override
    public boolean test(@NotNull FilterData data) {
        MsgGet msgget = data.getMsgGet();
        if (msgget instanceof GroupMsg) {
            String msg = ((GroupMsg) msgget).getMsg().trim();
            msg = CQ.UTILS.remove(msg);
            return "退群".equals(msg.trim()) && data.getAtDetection().atBot();
        }
        return false;
    }
}
