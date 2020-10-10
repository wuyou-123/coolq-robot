package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.utils.CQ;

import java.util.Objects;

/**
 * @author Administrator<br>
 * 2020年5月2日
 */
@Beans
public class AutisticFilter {

    @DIYFilter("autistic1")
    public class Autistic1 implements Filterable {

        @Override
        public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
            String message = msgget.getMsg();
            if (message.equals("自闭")) {
                return true;
            }
            final String msg = CQ.utils.remove(message, true, true);
            return at.test() && Objects.equals(CQ.getAt(message), msgget.getThisCode()) && msg.equals("自闭");
        }
    }

    @DIYFilter("autistic2")
    public class Autistic2 implements Filterable {

        @Override
        public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
            String message = msgget.getMsg();
            if (message.startsWith("领取套餐")) {
                return true;
            }
            final String msg = CQ.utils.remove(message, true, true);
            return at.test() && Objects.equals(CQ.getAt(message), msgget.getThisCode()) && msg.startsWith("领取套餐");
        }

    }
}
