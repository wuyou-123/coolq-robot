package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.utils.GlobalVariable;

/**
 * 获取现在机器人是否开机
 *
 * @author Administrator<br>
 * 2020年5月2日
 */
@DIYFilter("boot")
public class BootFilter implements Filterable {

    @Override
    public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
        if (msgget instanceof GroupMsg) {
            GroupMsg sender = (GroupMsg) msgget;
            Boolean ret = GlobalVariable.bootMap.get(sender.getGroup());
            return ret != null && ret;
        }
        return false;
    }

}
