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
@Component
public class GroupManagementFilter {

    @Component("kickMember")
    public static class KickMember implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return msg.startsWith("踢") && CQ.getAt(msg) != null;
            }
            return false;
        }
    }

    @Component("banMember")
    public static class BanMember implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return msg.startsWith("禁言") && CQ.getAt(msg) != null;
            }
            return false;
        }
    }

    @Component("cancelBanMember")
    public static class CancelBanMember implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return msg.startsWith("解禁") && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

    @Component("blackMember")
    public static class BlackMember implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                if (msg.startsWith("拉黑")) {
                    if (CQ.getAts(msg).size() > 0) {
                        return true;
                    }
                    String[] users = msg.substring(msg.indexOf("拉黑") + 2).split(",");
                    for (String user1 : users) {
                        for (String user2 : user1.split("，")) {
                            try {
                                Long.parseLong(user2);
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        }
                    }
                }
            }
            return false;
        }

    }

    @Component("cancelBlackMember")
    public static class CancelBlackMember implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                if (msg.startsWith("取消拉黑")) {
                    if (CQ.getAts(msg).size() > 0) {
                        return true;
                    }
                    String[] users = msg.substring(msg.indexOf("取消拉黑") + 4).split(",");
                    for (String user1 : users) {
                        for (String user2 : user1.split("，")) {
                            try {
                                Long.parseLong(user2);
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        }
                    }
                }
            }
            return false;
        }

    }

    @Component("changeNick")
    public static class ChangeNick implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return msg.startsWith("改名") && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

    @Component("changeTitle")
    public static class ChangeTitle implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return msg.startsWith("给头衔") && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

    @Component("addManager")
    public static class AddManager implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return (msg.startsWith("添加管理") || msg.startsWith("设置管理") || msg.startsWith("给管理")) && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

    @Component("removeManager")
    public static class RemoveManager implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return (msg.startsWith("删除管理") || msg.startsWith("取消管理") || msg.startsWith("移除管理")) && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

    @Component("addGroupManager")
    public static class AddGroupManager implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return (msg.startsWith("添加群管理") || msg.startsWith("设置群管理") || msg.startsWith("给群管理"))
                        && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

    @Component("removeGroupManager")
    public static class RemoveGroupManager implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String msg = ((GroupMsg) msgget).getMsg().trim();
                return (msg.startsWith("删除群管理") || msg.startsWith("取消群管理") || msg.startsWith("移除群管理"))
                        && CQ.getAt(msg) != null;
            }
            return false;
        }

    }

}
