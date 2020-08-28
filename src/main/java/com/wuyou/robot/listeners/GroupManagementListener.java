package com.wuyou.robot.listeners;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.MostDIYType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.JSONUtils;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.BlackUserService;
import com.wuyou.service.ClearService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class GroupManagementListener {
    @Depend
    BlackUserService blackUserService;
    @Depend
    ClearService clearService;
    List<String> administrator = new ArrayList<String>();

    public GroupManagementListener() {
        administrator.add("1097810498");
        administrator.add("1041025733");
        administrator.add("2973617637");
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "kickMember"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void kickMember(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        Set<String> set = CQ.getAts(msg.getMsg());
        if (set.size() == 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, "指令不合法,请至少艾特一位群成员");
            return;
        }
        if (getPower(msg, sender)) {
            StringBuilder str = new StringBuilder("\n踢人:");
            for (String qq : set) {
                String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
                if (qq.equals(msg.getThisCode())) {
                    str.append("\n\t\t踢出成员[" + qq + "](" + nickname + ")失败,我踢我自己？");
                    continue;
                }
                if (administrator.contains(qq)) {
                    str.append("\n\t\t踢出成员[" + qq + "](" + nickname + ")失败,不可以踢我的主人!!");
                    continue;
                }
                if (PowerUtils.powerCompare(msg, qq, sender)) {
                    sender.SETTER.setGroupMemberKick(fromGroup, qq, false);
                    str.append("\n\t\t踢出成员[" + qq + "](" + nickname + ")成功");
                } else {
                    str.append("\n\t\t踢出成员[" + qq + "](" + nickname + ")失败,我没有踢出TA的权限");
                }

            }
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "banMember"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void banMember(GroupMsg msg, MsgSender sender) {
        System.out.println("GroupManagementListener.banMember()");
        if (getPower(msg, sender)) {
            String message = msg.getMsg();
            String fromGroup = msg.getGroup();
            String fromQQ = msg.getQQ();
            Set<String> set = CQ.getAts(message);
            if (set.size() == 0) {
                SenderUtil.sendGroupMsg(sender, fromGroup, "指令不合法,请至少艾特一位群成员");
                return;
            }
            long time;
            try {
                time = Long.valueOf(message.split(" ")[message.split(" ").length - 1]);
            } catch (NumberFormatException e) {
                SenderUtil.sendGroupMsg(sender, fromGroup, "指令不合法,请在后面添加纯数字,单位:分钟");
                return;
            }
            try {
                StringBuilder str = new StringBuilder("\n禁言:");
                for (String qq : set) {
                    String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
                    if (qq.equals(msg.getThisCode())) {
                        str.append("\n\t\t禁言成员[" + qq + "](" + nickname + ")失败,我禁我自己？");
                        continue;
                    }
                    if (administrator.contains(qq)) {
                        str.append("\n\t\t禁言成员[" + qq + "](" + nickname + ")失败,不可以禁言我的主人!!");
                        continue;
                    }
                    boolean a = PowerUtils.powerCompare(msg, qq, sender);
                    System.out.println(a);
                    if (a) {
                        if (time > 1440 * 30) {
                            str.append("\n\t\t禁言成员[" + qq + "](" + nickname + ")失败,禁言时间不能超过30天!");
                            continue;
                        }
                        String times = time + "分钟";
                        if (time > 1440) {
                            times = time / 1440 + "天" + time % 1440 / 60 + "小时" + time % 1440 % 60 + "分钟";
                        } else if (time > 60) {
                            times = time / 60 + "小时" + time % 60 + "分钟";
                        }
                        str.append("\n\t\t禁言成员[" + qq + "](" + nickname + ")成功,禁言时长:"
                                + times.replace("小时0分钟", "小时").replace("分钟0秒", "分钟"));
                        sender.SETTER.setGroupBan(fromGroup, qq, time * 60);
                        continue;
                    } else {
                        str.append("\n\t\t禁言成员[" + qq + "](" + nickname + ")失败,我没有禁言TA的权限");
                    }
                }
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
            } catch (Exception e) {
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "指令不合法");
            }
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "cancelBanMember"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void cancelBanMember(GroupMsg msg, MsgSender sender) {
        if (getPower(msg, sender)) {
            String message = msg.getMsg();
            String fromGroup = msg.getGroup();
            String fromQQ = msg.getQQ();
            Set<String> set = CQ.getAts(message);
            if (set.size() == 0) {
                SenderUtil.sendGroupMsg(sender, fromGroup, "指令不合法,请至少艾特一位群成员");
                return;
            }
            StringBuilder str = new StringBuilder("\n解禁:");
            for (String qq : set) {
                GroupMemberInfo member = sender.GETTER.getGroupMemberInfo(fromGroup, qq);
                String nickname = member.getRemarkOrNickname();
                if (qq.equals(msg.getThisCode())) {
                    str.append("\n\t\t解禁成员[" + qq + "](" + nickname + ")失败,我要是被禁言了能发出这条消息?");
                    continue;
                }

                if (member.getBanTime() == 0) {
                    str.append("\n\t\t解禁成员[" + qq + "](" + nickname + ")失败,此用户不需要解禁");
                    continue;
                }

                if (PowerUtils.powerCompare(msg, qq, sender)) {
                    sender.SETTER.setGroupBan(fromGroup, qq, 0);
                    str.append("\n\t\t解禁成员[" + qq + "](" + nickname + ")成功");
                } else {
                    str.append("\n\t\t解禁成员[" + qq + "](" + nickname + ")失败,我没有解禁TA的权限");
                }
            }
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "blackMember"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void blackMember(GroupMsg msg, MsgSender sender) {
        if (getPower(msg, sender)) {
            String message = msg.getMsg();
            Set<String> set = CQ.getAts(message);
            if (set.size() != 0) {
                setBlackUsers(msg, sender, set);
            } else {
                String[] users = message.substring(message.indexOf("拉黑") + 2).split(",");
                for (String user1 : users) {
                    for (String user2 : user1.split("，")) {
                        set.add(user2);
                    }
                }
                setBlackUsers(msg, sender, set);
            }
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "cancelBlackMember"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void cancelBlackMember(GroupMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        Set<String> set = CQ.getAts(message);
        if (set.size() != 0) {
            cancelBlackUsers(msg, sender, set);
        } else {
            String[] users = message.substring(message.indexOf("取消拉黑") + 4).split(",");
            for (String user1 : users) {
                for (String user2 : user1.split("，")) {
                    set.add(user2);
                }
            }
            cancelBlackUsers(msg, sender, set);
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = "黑名单")
    public void allBlackMember(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        List<String> list = blackUserService.getUserByGroupId(fromGroup);
        StringBuilder str = new StringBuilder("\n黑名单:");
        int num = 0;
        for (String user : list) {
            System.out.println(user);
            num++;
            str.append("\n\t\t黑名单成员" + num + ": " + user);
        }
        if (num == 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, "暂无黑名单记录");
        } else {
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "changeNick"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void changeNick(GroupMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        String thisQQ = msg.getThisCode();
        Set<String> set = CQ.getAts(message);
        if (set.size() == 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, "指令不合法,请至少艾特一位群成员");
            return;
        }
        if (PowerUtils.getPowerType(fromGroup, fromQQ, sender) > 1) {

            if (message.startsWith("改名") && set.size() == 1 && CQ.getAt(message).equals(thisQQ)) {
                String name = message.split(" ")[message.split(" ").length - 1];
                if (CQ.getAt(name) == null) {
                    sender.SETTER.setGroupCard(fromGroup, thisQQ, name);
                    SenderUtil.sendGroupMsg(sender, fromGroup, "已把我的名字改为: \"" + name + "\"");
                } else {
                    sender.SETTER.setGroupCard(fromGroup, thisQQ, "");
                    SenderUtil.sendGroupMsg(sender, fromGroup, "已取消我的群名片");
                }
                return;
            }
        }
        if (getPower(msg, sender)) {
            try {
                StringBuilder str = new StringBuilder("\n改名片:");
                String name = message.split(" ")[message.split(" ").length - 1];
                if (CQ.getAt(name) == null) {
                    for (String qq : set) {
                        if (qq.equals(thisQQ)) {
                            sender.SETTER.setGroupCard(fromGroup, thisQQ, name);
                            str.append("\n\t\t已把我的名字改为: \"" + name + "\"");
                            continue;
                        }
                        String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
                        sender.SETTER.setGroupCard(fromGroup, qq, name);
                        str.append("\n\t\t已将成员[" + qq + "](" + nickname + ")群名片改为: \"" + name + "\"");
                    }
                } else {
                    for (String qq : set) {
                        if (qq.equals(thisQQ)) {
                            sender.SETTER.setGroupCard(fromGroup, thisQQ, "");
                            str.append("\n\t\t已取消我的群名片");
                            continue;
                        }
                        String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
                        sender.SETTER.setGroupCard(fromGroup, qq, "");
                        str.append("\n\t\t已取消成员[" + qq + "](" + nickname + ")的群名片");
                    }
                }
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
            } catch (Exception e) {
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "\n指令不合法");
            }
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "changeTitle"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void changeTitle(GroupMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        String thisQQ = msg.getThisCode();
        Set<String> set = CQ.getAts(message);
        if (set.size() == 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, "指令不合法,请至少艾特一位群成员");
            return;
        }
        if (sender.GETTER.getGroupMemberInfo(fromGroup, thisQQ).getPowerType().isOwner()) {
            try {
                StringBuilder str = new StringBuilder("\n设置头衔:");
                String name = message.split(" ")[message.split(" ").length - 1];
                if (CQ.getAt(name) == null) {
                    for (String qq : set) {
                        if (qq.equals(thisQQ)) {
                            sender.SETTER.setGroupExclusiveTitle(fromGroup, thisQQ, name, -1);
                            str.append("\n\t\t已把我的头衔设置为: " + name);
                            continue;
                        }
                        String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
                        sender.SETTER.setGroupExclusiveTitle(fromGroup, qq, name, -1);
                        str.append("\n\t\t已将成员[" + qq + "](" + nickname + ")的群头衔设置为: \"" + name + "\"");
                    }
                } else {
                    for (String qq : set) {
                        String title = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getExTitle();
                        if (qq.equals(thisQQ)) {
                            if ("".equals(title)) {
                                str.append("\n\t\t取消失败,我没有头衔");
                                continue;
                            }
                            sender.SETTER.setGroupExclusiveTitle(fromGroup, qq, name, 0);
                            str.append("\n\t\t已取消我的群头衔");
                            continue;
                        }
                        String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
                        if ("".equals(title)) {
                            str.append("\n\t\t取消失败,成员[" + qq + "](" + nickname + ")没有头衔");
                            continue;
                        }
                        sender.SETTER.setGroupExclusiveTitle(fromGroup, qq, "", -1);
                        str.append("\n\t\t已取消成员[" + qq + "](" + nickname + ")的群头衔");
                    }
                }
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
            } catch (Exception e) {
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "\n指令不合法");
            }
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = "全体禁言")
    public void banAll(GroupMsg msg, MsgSender sender) {
        if (getPower(msg, sender)) {
            sender.SETTER.setGroupWholeBan(msg.getGroup(), true);
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = "全体解禁")
    public void cancleBanAll(GroupMsg msg, MsgSender sender) {
        if (getPower(msg, sender)) {
            sender.SETTER.setGroupWholeBan(msg.getGroup(), false);
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = "禁言列表")
    public void banList(GroupMsg msg, MsgSender sender) throws IOException {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        StringBuilder str = new StringBuilder("\n禁言列表: ");
        String cookiesStr = sender.GETTER.getAuthInfo().getCookies();
        var cookies = new HashMap<String, String>();
        for (String cookie : cookiesStr.split(";")) {
            cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
        }
        String bkn = sender.GETTER.getAuthInfo().getCsrfToken();
        String url = "https://qinfo.clt.qq.com/cgi-bin/qun_info/get_group_setting_v2?src=qinfo_v3&gc=" + fromGroup
                + "&bkn=" + bkn;
        String body = Jsoup.connect(url).ignoreContentType(true).cookies(cookies).get().text();
        JSONObject json = JSONUtils.toJsonObject(body);
        System.out.println(json);
        JSONObject shutup = json.getJSONObject("shutup");
        JSONArray list = shutup.getJSONArray("list");
        if (list == null) {
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "当前群内没有被禁言的群员");
            return;
        }
        int num = 0;
        for (Object object : list) {
            num++;
            JSONObject j = (JSONObject) object;
            int time = j.getInteger("t");
            String qq = j.getString("uin");
            String nick = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
            String times = "";
            if (time > 1440 * 60) {
                times = time / 1440 / 60 + "天" + (time / 60) % 1440 / 60 + "小时" + (time / 60) % 1440 % 60 + "分钟";
            } else if (time > 60 * 60) {
                times = time / 60 / 60 + "小时" + time / 60 % 60 + "分钟";
            } else {
                times = time / 60 + "分钟" + time % 60 + "秒";
            }
            str.append("\n\t群成员" + num + ": QQ:[" + qq + "](" + nick + "), 禁言时间: "
                    + times.replace("0小时", "").replace("0分钟", "").replace("0秒", ""));
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = ".*艾特全体.*", at = true)
    public void atAll(GroupMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        if (getPower(msg, sender)) {
            SenderUtil.sendGroupMsg(sender, msg.getGroup(),
                    CQ.at("all") + " " + message.substring(message.indexOf("艾特全体") + 4));
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "leave", at = true)
    public void leaveGroup(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroup();
        if (sender.GETTER.getGroupMemberInfo(group, msg.getThisCode()).getPowerType().isOwner()) {
            sender.SENDER.sendPrivateMsg(msg.getQQ(), "退出失败,不能退出我的群");
            return;
        }
        sender.SETTER.setGroupLeave(group);
        clearService.clearAllData(group);
    }

    /**
     * 判断权限
     *
     * @param msg
     * @param sender
     * @return 发送人有机器人管理权限或是群主, 并且机器人是管理员/群主时返回true
     */
    private boolean getPower(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroup();
        if (PowerUtils.getPowerType(group, msg.getQQ(), sender) > 1) {
            int power = PowerUtils.getPowerType(group, msg.getThisCode(), sender);
            if (power == 3 || power == 1) {
                return true;
            } else
                SenderUtil.sendGroupMsg(sender, group, "添加失败,我没有管理员权限!");
        } else
            SenderUtil.sendGroupMsg(sender, group, "操作失败,你不是我的管理员!");
        return false;
    }

    private void setBlackUsers(GroupMsg msg, MsgSender sender, Set<String> set) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        StringBuilder str = new StringBuilder("\n添加黑名单:");

        for (String user : set) {
            GroupMemberInfo userMember;
            try {
                userMember = sender.GETTER.getGroupMemberInfo(fromGroup, user);
            } catch (Exception e) {
                try {
                    if (administrator.contains(user)) {
                        str.append("\n\t\t添加黑名单失败: QQ:[" + user + "],不可以拉黑我的主人!");
                        continue;
                    }
                    blackUserService.addBlackUser(fromGroup, user);
                    str.append("\n\t\t添加黑名单成功: QQ:[" + user + "]已添加");
                } catch (ObjectExistedException e2) {
                    str.append("\n\t\t添加黑名单失败: QQ:[" + user + "]已存在");
                }
                continue;
            }
            String nickname = userMember.getRemarkOrNickname();
            if (administrator.contains(user)) {
                str.append("\n\t\t添加黑名单失败: QQ:[" + user + "](" + nickname + "),不可以拉黑我的主人!");
                continue;
            }
            if (user.equals(msg.getThisCode())) {
                str.append("\n\t\t添加黑名单失败: QQ:[" + user + "](" + nickname + "),我这辈子不可能拉黑自己!");
                continue;
            }
            if (PowerUtils.powerCompare(msg, user, sender)) {
                sender.SETTER.setGroupMemberKick(fromGroup, user, true);
            } else {
                str.append("\n\t\t添加黑名单失败: QQ:[" + user + "](" + nickname + "),我没有权限对TA进行操作");
                continue;
            }
            try {
                blackUserService.addBlackUser(fromGroup, user);
                str.append("\n\t\t添加黑名单成功: QQ:[" + user + "](" + nickname + ")已添加");
            } catch (ObjectExistedException e2) {
                str.append("\n\t\t添加黑名单失败: QQ:[" + user + "](" + nickname + ")已存在");
            }
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);
    }

    private void cancelBlackUsers(GroupMsg msg, MsgSender sender, Set<String> set) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        StringBuilder str = new StringBuilder("\n移除黑名单:");

        for (String user : set) {
            try {
                blackUserService.removeBlackUser(fromGroup, user);
                str.append("\n\t取消成功: QQ:[" + user + "]已移除黑名单");
            } catch (ObjectNotFoundException e) {
                str.append("\n\t取消失败: QQ:[" + user + "]不在黑名单");
                continue;
            }
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + str);

    }

}
