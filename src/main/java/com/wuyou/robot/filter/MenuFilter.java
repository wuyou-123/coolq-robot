package com.wuyou.robot.filter;

import com.wuyou.utils.CQ;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取现在机器人是否开机
 *
 * @author Administrator<br>
 * 2020年5月2日
 */
@Component("menu")
public class MenuFilter implements ListenerFilter {

    @Override
    public boolean test(@NotNull FilterData data) {
        List<String> list = new ArrayList<>();
        list.add("菜单");
        list.add("功能");
        MsgGet msgget = data.getMsgGet();
        if (msgget instanceof GroupMsg) {
            String message = ((GroupMsg) msgget).getMsg().trim();
            String msg = CQ.UTILS.remove(message, true, true);
            return list.contains(msg);
        }
        return false;
    }

}
