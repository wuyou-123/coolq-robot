package com.wuyou.robot.listeners;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.CQCodeTypes;
import com.forte.qqrobot.beans.types.MostDIYType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.ManagerService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator<br>
 * 2020年3月13日
 */
@Beans
public class GroupManagerListener {
    @Depend
    ManagerService service;
    List<String> administrator = new ArrayList<String>();

    public GroupManagerListener() {
        administrator.add("1097810498");
        administrator.add("1041025733");
        administrator.add("2973617637");
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = "管理员列表", diyFilter = "boot")
    public void sendManager(GroupMsg msg, MsgSender sender) {
        System.out.println("发送管理员列表");
        String fromGroup = msg.getGroup();
        List<String> list = service.getManagerByGroupId(msg.getGroup());
        StringBuilder str = new StringBuilder("管理员列表:\n");
        int a = 0;
        Set<String> members = new HashSet<String>();
        for (String qq : list) {
            try {
                sender.GETTER.getGroupMemberInfo(fromGroup, qq);
            } catch (Exception e) {
                members.add(qq);
                continue;
            }
            a++;
            String nickname = sender.GETTER.getGroupMemberInfo(fromGroup, qq).getRemarkOrNickname();
            str.append("\t管理员" + a + ": " + qq + "(" + nickname + ")\n");
        }
        if (members.size() > 0) {
            removeManager(members, fromGroup, sender);
        }
        if (a > 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, str.toString().trim());
        } else {
            SenderUtil.sendGroupMsg(sender, fromGroup, "暂无管理员");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "addManager"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void addManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroup();
        if (PowerUtils.getPowerType(group, msg.getQQ(), sender) > 1) {
            Set<String> set = new HashSet<String>();
            for (CQCode user : CQCodeUtil.build().getCQCodeFromMsgByType(msg.getMsg(), CQCodeTypes.at)) {
                set.add(user.get("qq"));
            }
            addManagers(msg, sender);
        } else {
            SenderUtil.sendGroupMsg(sender, group, "添加失败,你不是我的管理员!");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "removeManager"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void removeManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroup();
        if (PowerUtils.getPowerType(group, msg.getQQ(), sender) > 1) {
            Set<String> set = new HashSet<String>();
            for (CQCode user : CQCodeUtil.build().getCQCodeFromMsgByType(msg.getMsg(), CQCodeTypes.at)) {
                set.add(user.get("qq"));
            }
            removeManager(set, group, sender);
        } else {
            SenderUtil.sendGroupMsg(sender, group, "删除失败,你不是我的管理员!");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "addGroupManager"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void addGroupManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroup();
        if (PowerUtils.getPowerType(group, msg.getQQ(), sender) > 1) {
            if (PowerUtils.getPowerType(group, msg.getThisCode(), sender) == 3) {
                Set<String> set = new HashSet<String>();
                for (CQCode user : CQCodeUtil.build().getCQCodeFromMsgByType(msg.getMsg(), CQCodeTypes.at)) {
                    set.add(user.get("qq"));
                }
                addGroupManager(set, group, sender);
            } else {
                SenderUtil.sendGroupMsg(sender, group, "添加失败,我不是群主!");
            }
        } else {
            SenderUtil.sendGroupMsg(sender, group, "添加失败,你不是我的管理员!");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "removeGroupManager"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void removeGroupManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroup();
        if (PowerUtils.getPowerType(group, msg.getQQ(), sender) > 1) {
            if (PowerUtils.getPowerType(group, msg.getThisCode(), sender) == 3) {
                Set<String> set = new HashSet<String>();
                for (CQCode user : CQCodeUtil.build().getCQCodeFromMsgByType(msg.getMsg(), CQCodeTypes.at)) {
                    set.add(user.get("qq"));
                }
                removeGroupManager(set, group, sender);
            } else {
                SenderUtil.sendGroupMsg(sender, group, "添加失败,我不是群主!");
            }
        } else {
            SenderUtil.sendGroupMsg(sender, group, "添加失败,你不是我的管理员!");
        }
    }

    private void addGroupManager(Set<String> list, String fromGroup, MsgSender sender) {
        StringBuilder str = new StringBuilder("设置群管理员:");
        for (String user : list) {
            GroupMemberInfo userMember = sender.GETTER.getGroupMemberInfo(fromGroup, user);
            String nickname = userMember.getRemarkOrNickname();
            int power = PowerUtils.getPowerType(fromGroup, user, sender);
            System.out.println(power);
            switch (power) {
                case 4:
                    if (sender.GETTER.getGroupMemberInfo(fromGroup, user).getPowerType().isMember()) {
                        sender.SETTER.setGroupAdmin(fromGroup, user, true);
                        str.append("\n\t\t设置群管理员成功: QQ:[" + user + "](" + nickname + "),恭喜主人升级为管理员");
                    } else {
                        str.append("\n\t\t设置群管理员失败: QQ:[" + user + "](" + nickname + "),我的主人本来就是管理员");
                    }
                    break;
                case 3:
                    str.append("\n\t\t设置群管理员失败: QQ:[" + user + "](" + nickname + "),想把我降级成管理员???");
                    break;
                case 1:
                    str.append("\n\t\t设置群管理员失败, [" + user + "](" + nickname + ")已经是管理员了");
                    break;
                default:
                    sender.SETTER.setGroupAdmin(fromGroup, user, true);
                    str.append("\n\t\t设置群管理员成功, [" + user + "](" + nickname + ")已经被设置为管理员");
                    break;
            }
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, str.toString());
    }

    /**
     * @param list
     * @param fromGroup
     * @param sender
     */
    private void removeGroupManager(Set<String> list, String fromGroup, MsgSender sender) {
        StringBuilder str = new StringBuilder("取消群管理员:");
        for (String user : list) {
            GroupMemberInfo userMember = sender.GETTER.getGroupMemberInfo(fromGroup, user);
            String nickname = userMember.getRemarkOrNickname();
            int power = PowerUtils.getPowerType(fromGroup, user, sender);
            if (power == 1) {
                sender.SETTER.setGroupAdmin(fromGroup, user, false);
                str.append("\n\t\t设置管理员成功, [" + user + "](" + nickname + ")已经被设置为管理员");
                continue;
            }

            switch (power) {
                case 4:
                    if (sender.GETTER.getGroupMemberInfo(fromGroup, user).getPowerType().isAdmin()) {
                        sender.SETTER.setGroupAdmin(fromGroup, user, false);
                        str.append("\n\t\t取消群管理员成功: QQ:[" + user + "](" + nickname + "),已把我的主人降级为群员");
                    } else {
                        str.append("\n\t\t取消群管理员失败, [" + user + "](" + nickname + ")不是管理员");
                    }
                    break;
                case 3:
                    str.append("\n\t\t取消群管理员失败: QQ:[" + user + "](" + nickname + "),我本来就不是管理员!!");
                    break;
                case 2:
                    sender.SETTER.setGroupAdmin(fromGroup, user, false);
                    str.append("\n\t\t取消群管理员成功, [" + user + "](" + nickname + ")已经被取消管理员");
                    break;
                default:
                    str.append("\n\t\t取消群管理员失败, [" + user + "](" + nickname + ")不是管理员");
                    break;
            }
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, str.toString());
    }

    private void addManagers(GroupMsg msg, MsgSender sender) {
        Set<String> list = CQ.getAts(msg.getMsg());
        String fromGroup = msg.getGroup();
        StringBuilder str = new StringBuilder("添加管理员:");
        for (String user : list) {
            GroupMemberInfo userMember = sender.GETTER.getGroupMemberInfo(fromGroup, user);
            String nickname = userMember.getRemarkOrNickname();
            if (administrator.contains(user)) {
                str.append("\n\t\t添加管理员失败: QQ:[" + user + "](" + nickname + "),不需要添加我的主人!");
                continue;
            }
            if (user.equals(msg.getThisCode())) {
                str.append("\n\t\t添加管理员失败: QQ:[" + user + "](" + nickname + "),怎么可以把自己设置为我的管理员。。。");
                continue;
            }
            try {
                service.addManager(fromGroup, user);
                str.append("\n\t\t添加管理员成功: QQ:[" + user + "](" + nickname + ")已添加");
            } catch (ObjectExistedException e) {
                str.append("\n\t\t添加管理员失败: QQ:[" + user + "](" + nickname + ")已存在");
                continue;
            }
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, str.toString());
    }

    private void removeManager(Set<String> list, String fromGroup, MsgSender sender) {
        StringBuilder str = new StringBuilder("取消管理员");
        for (String user : list) {
            try {
                service.removeManager(fromGroup, user);
            } catch (ObjectNotFoundException e) {
                str.append("\n\t取消失败: QQ:[" + user + "]不是我的管理员");
                continue;
            }
            str.append("\n\t取消成功: QQ:[" + user + "]已取消管理员");
//			managers.get(fromGroup).remove(user);
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, str.toString());
    }
}
