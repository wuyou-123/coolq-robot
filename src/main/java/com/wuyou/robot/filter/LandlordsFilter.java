package com.wuyou.robot.filter;

import com.wuyou.utils.GlobalVariable;
import lombok.SneakyThrows;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author wuyou
 */
@Component
public class LandlordsFilter {

    @Component("privateLandlords")
    public static class PrivateMsgFilter implements ListenerFilter {
        @Override
        public boolean test(@NotNull FilterData data) {
            if (data.getMsgGet() instanceof PrivateMsg) {
                PrivateMsg msg = (PrivateMsg) data.getMsgGet();
                return GlobalVariable.LANDLORDS_PLAYER.containsKey(msg.getAccountInfo().getAccountCode());
            }
            return false;
        }
    }

    @Component("groupLandlords")
    public static class GroupMsgFilter implements ListenerFilter {
        @SneakyThrows
        @Override
        public boolean test(@NotNull FilterData data) {
            if (data.getMsgGet() instanceof GroupMsg) {
                GroupMsg msg = (GroupMsg) data.getMsgGet();
                return GlobalVariable.LANDLORDS_PLAYER.containsKey(msg.getAccountInfo().getAccountCode());
            }
            return false;
        }
    }
}
