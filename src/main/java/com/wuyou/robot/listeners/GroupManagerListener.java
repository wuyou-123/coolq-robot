package com.wuyou.robot.listeners;

import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.ManagerService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MostMatchType;

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

    @OnGroup
    @Filters(value = @Filter(value = "管理员列表"), customFilter = "boot")
    public void sendManager(GroupMsg msg, MsgSender sender) {
        System.out.println("发送管理员列表");
        String fromGroup = msg.getGroupInfo().getGroupCode();
        List<String> list = service.getManagerByGroupId(msg.getGroupInfo().getGroupCode());
        StringBuilder str = new StringBuilder("管理员列表:\n");
        int a = 0;
        Set<String> members = new HashSet<>();
        for (String qq : list) {
            try {
                sender.GETTER.getMemberInfo(fromGroup, qq);
            } catch (Exception e) {
                members.add(qq);
                continue;
            }
            a++;
            String nickname = sender.GETTER.getMemberInfo(fromGroup, qq).getAccountInfo().getAccountRemarkOrNickname();
            str.append("\t管理员").append(a).append(": ").append(qq).append("(").append(nickname).append(")\n");
        }
        if (members.size() > 0) {
            removeManager(members, fromGroup, sender);
        }
        if (a > 0) {
            SenderUtil.sendGroupMsg(fromGroup, str.toString().trim());
        } else {
            SenderUtil.sendGroupMsg(fromGroup, "暂无管理员");
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "addManager"}, customMostMatchType = MostMatchType.ALL)
    public void addManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
        if (PowerUtils.getPermissions(group, msg.getAccountInfo().getAccountCode(), sender) > 1) {
            addManagers(msg, sender);
        } else {
            SenderUtil.sendGroupMsg(group, "添加失败,你不是我的管理员!");
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "removeManager"}, customMostMatchType = MostMatchType.ALL)
    public void removeManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
        if (PowerUtils.getPermissions(group, msg.getAccountInfo().getAccountCode(), sender) > 1) {
            Set<String> set = new HashSet<>(CQ.getAts(msg.getMsg()));
            removeManager(set, group, sender);
        } else {
            SenderUtil.sendGroupMsg(group, "删除失败,你不是我的管理员!");
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "addGroupManager"}, customMostMatchType = MostMatchType.ALL)
    public void addGroupManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
        if (PowerUtils.getPermissions(group, msg.getAccountInfo().getAccountCode(), sender) > 1) {
            if (PowerUtils.getPermissions(group, msg.getBotInfo().getBotCode(), sender) == 3) {
                Set<String> set = new HashSet<>(CQ.getAts(msg.getMsg()));
                addGroupManager(set, group, sender);
            } else {
                SenderUtil.sendGroupMsg(group, "添加失败,我不是群主!");
            }
        } else {
            SenderUtil.sendGroupMsg(group, "添加失败,你不是我的管理员!");
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "removeGroupManager"}, customMostMatchType = MostMatchType.ALL)
    public void removeGroupManager(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
        if (PowerUtils.getPermissions(group, msg.getAccountInfo().getAccountCode(), sender) > 1) {
            if (PowerUtils.getPermissions(group, msg.getBotInfo().getBotCode(), sender) == 3) {
                Set<String> set = new HashSet<>(CQ.getAts(msg.getMsg()));
                removeGroupManager(set, group, sender);
            } else {
                SenderUtil.sendGroupMsg(group, "添加失败,我不是群主!");
            }
        } else {
            SenderUtil.sendGroupMsg(group, "添加失败,你不是我的管理员!");
        }
    }

    private void addGroupManager(Set<String> list, String fromGroup, MsgSender sender) {
        StringBuilder str = new StringBuilder("设置群管理员:");
        for (String user : list) {
            GroupMemberInfo userMember = sender.GETTER.getMemberInfo(fromGroup, user);
            String nickname = userMember.getAccountInfo().getAccountRemarkOrNickname();
            int power = PowerUtils.getPermissions(fromGroup, user, sender);
            System.out.println(power);
            switch (power) {
                case 4:
                    if (sender.GETTER.getMemberInfo(fromGroup, user).getPermission().isMember()) {
                        sender.SETTER.setGroupAdmin(fromGroup, user, true);
                        str.append("\n\t\t设置群管理员成功: QQ:[").append(user).append("](").append(nickname).append("),恭喜主人升级为管理员");
                    } else {
                        str.append("\n\t\t设置群管理员失败: QQ:[").append(user).append("](").append(nickname).append("),我的主人本来就是管理员");
                    }
                    break;
                case 3:
                    str.append("\n\t\t设置群管理员失败: QQ:[").append(user).append("](").append(nickname).append("),想把我降级成管理员???");
                    break;
                case 1:
                    str.append("\n\t\t设置群管理员失败, [").append(user).append("](").append(nickname).append(")已经是管理员了");
                    break;
                default:
                    sender.SETTER.setGroupAdmin(fromGroup, user, true);
                    str.append("\n\t\t设置群管理员成功, [").append(user).append("](").append(nickname).append(")已经被设置为管理员");
                    break;
            }
        }
        SenderUtil.sendGroupMsg(fromGroup, str.toString());
    }

    /**
     * @param list      删除管理员的列表
     * @param fromGroup 群号
     * @param sender    sender
     */
    private void removeGroupManager(Set<String> list, String fromGroup, MsgSender sender) {
        StringBuilder str = new StringBuilder("取消群管理员:");
        for (String user : list) {
            GroupMemberInfo userMember = sender.GETTER.getMemberInfo(fromGroup, user);
            String nickname = userMember.getAccountInfo().getAccountRemarkOrNickname();
            int power = PowerUtils.getPermissions(fromGroup, user, sender);
            if (power == 1) {
                sender.SETTER.setGroupAdmin(fromGroup, user, false);
                str.append("\n\t\t设置管理员成功, [").append(user).append("](").append(nickname).append(")已经被设置为管理员");
                continue;
            }

            switch (power) {
                case 4:
                    if (sender.GETTER.getMemberInfo(fromGroup, user).getPermission().isAdmin()) {
                        sender.SETTER.setGroupAdmin(fromGroup, user, false);
                        str.append("\n\t\t取消群管理员成功: QQ:[").append(user).append("](").append(nickname).append("),已把我的主人降级为群员");
                    } else {
                        str.append("\n\t\t取消群管理员失败, [").append(user).append("](").append(nickname).append(")不是管理员");
                    }
                    break;
                case 3:
                    str.append("\n\t\t取消群管理员失败: QQ:[").append(user).append("](").append(nickname).append("),我本来就不是管理员!!");
                    break;
                case 2:
                    sender.SETTER.setGroupAdmin(fromGroup, user, false);
                    str.append("\n\t\t取消群管理员成功, [").append(user).append("](").append(nickname).append(")已经被取消管理员");
                    break;
                default:
                    str.append("\n\t\t取消群管理员失败, [").append(user).append("](").append(nickname).append(")不是管理员");
                    break;
            }
        }
        SenderUtil.sendGroupMsg(fromGroup, str.toString());
    }

    private void addManagers(GroupMsg msg, MsgSender sender) {
        Set<String> list = CQ.getAts(msg.getMsg());
        String fromGroup = msg.getGroupInfo().getGroupCode();
        StringBuilder str = new StringBuilder("添加管理员:");
        for (String user : list) {
            GroupMemberInfo userMember = sender.GETTER.getMemberInfo(fromGroup, user);
            String nickname = userMember.getAccountInfo().getAccountRemarkOrNickname();
            if (GlobalVariable.ADMINISTRATOR.contains(user)) {
                str.append("\n\t\t添加管理员失败: QQ:[").append(user).append("](").append(nickname).append("),不需要添加我的主人!");
                continue;
            }
            if (user.equals(msg.getBotInfo().getBotCode())) {
                str.append("\n\t\t添加管理员失败: QQ:[").append(user).append("](").append(nickname).append("),怎么可以把自己设置为我的管理员。。。");
                continue;
            }
            try {
                service.addManager(fromGroup, user);
                str.append("\n\t\t添加管理员成功: QQ:[").append(user).append("](").append(nickname).append(")已添加");
            } catch (ObjectExistedException e) {
                str.append("\n\t\t添加管理员失败: QQ:[").append(user).append("](").append(nickname).append(")已存在");
            }
        }
        SenderUtil.sendGroupMsg(fromGroup, str.toString());
    }

    private void removeManager(Set<String> list, String fromGroup, MsgSender sender) {
        StringBuilder str = new StringBuilder("取消管理员");
        for (String user : list) {
            try {
                service.removeManager(fromGroup, user);
            } catch (ObjectNotFoundException e) {
                str.append("\n\t取消失败: QQ:[").append(user).append("]不是我的管理员");
                continue;
            }
            str.append("\n\t取消成功: QQ:[").append(user).append("]已取消管理员");
///			managers.get(fromGroup).remove(user);
        }
        SenderUtil.sendGroupMsg(fromGroup, str.toString());
    }
}
