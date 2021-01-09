package com.wuyou.robot.filter;

import com.wuyou.utils.GlobalVariable;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 获取现在机器人是否开机
 *
 * @author wuyou<br>
 * 2020年5月2日
 */
@Component("boot")
public class BootFilter implements ListenerFilter {

    @Override
    public boolean test(@NotNull FilterData data) {
//        System.out.println(111111);
        MsgGet msgget = data.getMsgGet();
        if (msgget instanceof GroupMsg) {
            Boolean ret = GlobalVariable.BOOT_MAP.get(((GroupMsg) msgget).getGroupInfo().getGroupCode());
//            System.out.println(ret);
            return ret != null && ret;
        }
        return false;
    }

}
