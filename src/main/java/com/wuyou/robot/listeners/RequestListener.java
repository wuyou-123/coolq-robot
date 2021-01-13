package com.wuyou.robot.listeners;

import com.wuyou.entity.GroupMemberEntity;
import com.wuyou.enums.SexType;
import com.wuyou.service.AllBlackService;
import com.wuyou.service.BlackUserService;
import com.wuyou.service.StatService;
import com.wuyou.utils.*;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.assists.Permissions;
import love.forte.simbot.api.message.events.FriendAddRequest;
import love.forte.simbot.api.message.events.GroupAddRequest;
import love.forte.simbot.api.message.events.GroupMemberIncrease;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.MsgSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator<br>
 * 2020年3月13日
 */
@Beans
public class RequestListener {
    @Depend
    StatService statService;
    @Depend
    AllBlackService allBlackUserService;
    @Depend
    BlackUserService blackUserService;

    final String adminQQ = "1097810498";

    @OnFriendAddRequest
    public void friendAddRequest(FriendAddRequest request, MsgSender sender) {
        sender.SETTER.setFriendAddRequest(request.getFlag(), "", true, false);
        sender.SENDER.sendPrivateMsg(adminQQ,
                "已经添加[" + request.getAccountInfo().getAccountCode() + "](" + sender.GETTER.getFriendInfo(request.getAccountInfo().getAccountCode()).getAccountInfo().getAccountRemarkOrNickname() + ")为好友,验证消息为"
                        + ("".equals(request.getText()) ? "空" : ": " + request.getText()));
    }

    @OnGroupAddRequest
    public void groupAddRequest(GroupAddRequest request, MsgSender sender) {
        String fromGroup = request.getGroupInfo().getGroupCode();
        String qq = request.getAccountInfo().getAccountCode();
        Getter getter = sender.GETTER;

        List<String> list = getAllBlackUser(fromGroup);
        // 判断是不是黑名单里的成员
        if (list.contains(qq)) {
            sender.SETTER.setGroupAddRequest(request.getFlag(), false, true, "");
            sender.SENDER.sendPrivateMsg(adminQQ, "已经拒绝黑名单成员[" + request.getAccountInfo().getAccountCode() + "]("
                    + getter.getFriendInfo(qq).getAccountInfo().getAccountRemarkOrNickname() + ")加入群[" + request.getGroupInfo().getGroupCode() + "]("
                    + getter.getGroupInfo(request.getGroupInfo().getGroupCode()).getGroupName() + ")," + ("".equals(request.getText()) ? "附加消息为空"
                    : Objects.requireNonNull(request.getText()).contains("邀请人") ? request.getText() : "附加消息为: " + request.getText()));
            SenderUtil.sendGroupMsg(fromGroup, "已经拒绝黑名单成员[" + request.getAccountInfo().getAccountCode() + "]("
                    + getter.getFriendInfo(qq).getAccountInfo().getAccountRemarkOrNickname() + ")加入群, " + ("".equals(request.getText()) ? "附加消息为空"
                    : Objects.requireNonNull(request.getText()).contains("邀请人") ? request.getText() : "附加消息为: " + request.getText()));
            return;
        }

        if (request.getRequestType() == GroupAddRequest.Type.PASSIVE&& request.getAccountInfo().getAccountCode().equals(sender.GETTER.getBotInfo().getBotCode())) {
            // 邀请机器人进群
            List<String> allBlackGroups = allBlackUserService.getAllBlack(2);
            if (allBlackGroups.contains(request.getGroupInfo().getGroupCode())) {
                sender.SETTER.setGroupAddRequest(request.getFlag(), false, true, "");
                sender.SENDER.sendPrivateMsg(adminQQ,
                        "[" + request.getAccountInfo().getAccountCode() + "](" + getter.getFriendInfo(qq).getAccountInfo().getAccountRemarkOrNickname() + ")邀请我加入群["
                                + request.getGroupInfo().getGroupCode() + "](" + getter.getGroupInfo(request.getGroupInfo().getGroupCode()).getGroupName()
                                + "),但是由于此群在黑名单内被我拒绝了");
                return;
            }
            sender.SETTER.setGroupAddRequest(request.getFlag(), true, false, "");
            sender.SENDER.sendPrivateMsg(adminQQ,
                    "[" + request.getAccountInfo().getAccountCode() + "](" + getter.getFriendInfo(qq).getAccountInfo().getAccountRemarkOrNickname() + ")已邀请我加入群["
                            + request.getGroupInfo().getGroupCode() + "](" + getter.getGroupInfo(request.getGroupInfo().getGroupCode()).getGroupName() + ")");
        }
    }

    @OnGroupMemberIncrease
    public void groupMemberIncrease(GroupMemberIncrease increase, MsgSender sender) {
        Getter getter = sender.GETTER;
        String fromGroup = increase.getGroupInfo().getGroupCode();
        String beingOperateQQ = increase.getBeOperatorInfo().getBeOperatorCode();
        int level = LevelUtils.getLevel(beingOperateQQ);
        System.out.println("等级: " + level);
        String loginQQ = increase.getBotInfo().getBotCode();
        if (loginQQ.equals(beingOperateQQ)) {
            // 如果新成员是自己
            List<String> allBlackUsers = allBlackUserService.getAllBlack(2);
            if (allBlackUsers.contains(fromGroup)) {
                sender.SENDER.sendPrivateMsg(adminQQ,
                        "有人邀请我加入群[" + fromGroup + "](" + getter.getGroupInfo(fromGroup).getGroupName() + "),但是由于此群在黑名单内我退出了");
                sender.SETTER.setGroupQuit(fromGroup, false);
            }
            return;
        }
        try {
            List<String> list = getAllBlackUser(fromGroup);
            // 判断是不是黑名单里的成员
            if (list.contains(beingOperateQQ)) {
                GroupMemberInfo beingOperateMember = getter.getMemberInfo(fromGroup, beingOperateQQ);
                if (getter.getMemberInfo(fromGroup, loginQQ).getPermission() != Permissions.MEMBER) {
                    sender.SETTER.setGroupMemberKick(fromGroup, beingOperateQQ, "", true);
                    SenderUtil.sendGroupMsg(fromGroup,
                            "发现黑名单成员[" + beingOperateQQ + "](" + beingOperateMember.getAccountInfo().getAccountRemarkOrNickname() + ")入群,已将他移除此群");
                } else {
                    SenderUtil.sendGroupMsg(fromGroup, "发现黑名单成员[" + beingOperateQQ + "]("
                            + beingOperateMember.getAccountInfo().getAccountRemarkOrNickname() + ")入群,但是我没有权限将他移除此群");
                }
                return;

            }
            // 新人不是黑名单成员
            if (statService.getStat(fromGroup) == 1) {
//                List<GroupMemberEntity> groupMemberEntityList = GroupUtils.getGroupMembers(sender, fromGroup, beingOperateQQ);
                GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
//                if (groupMemberEntityList.size() != 0) {
//                    groupMemberEntity = groupMemberEntityList.get(0);
//                    System.out.println(groupMemberEntity);
//                }
                String end = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89";
                String welcomeMessage = "欢迎" + (groupMemberEntity.getSex() == SexType.MALE ? "新来的小哥哥"
                        : groupMemberEntity.getSex() == SexType.FEMALE ? "新来的小姐姐" : "新人") +
                        (level <= 3 && level > 0 ? "，你的等级小于三级,该不会是谁的小号吧"
                                : level >= 3 && level <= 5 ? "，你的等级为" + level + "级,有一点点低哦"
                                : level >= 5 && level <= 10 ? "，你的等级为" + level + "级,有一点低哦" : end);
                SenderUtil.sendGroupMsg(fromGroup, CQ.at(beingOperateQQ) + welcomeMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sender.SENDER.sendPrivateMsg(adminQQ, "出现异常");
            if ("null".equals(e.getMessage()) || e.getMessage() == null) {
                sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "空指针");
                return;
            }
            sender.SENDER.sendPrivateMsg(adminQQ, e.getMessage());
        }
    }

    private List<String> getAllBlackUser(String fromGroup) {
        List<String> allBlackUsers = allBlackUserService.getAllBlack(1);
        List<String> blackUsers = blackUserService.getUserByGroupId(fromGroup);

        List<String> list = new ArrayList<>();
        if (blackUsers.size() == 0 && allBlackUsers.size() == 0) {
            return list;
        }
        if (blackUsers.size() != 0) {
            // 判断群黑名单里是否有黑名单成员
            list.addAll(blackUsers);
        }
        if (allBlackUsers.size() != 0) {
            // 判断全局黑名单里是否有黑名单成员
            list.addAll(allBlackUsers);
        }
        return list;
    }

    @OnGroup
    @Filter("群活跃信息")
    public void testListen2(GroupMsg msg, MsgSender sender) {
        System.out.println("发送群活跃信息");
        SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(),
                "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + msg.getGroupInfo().getGroupCode());
    }

}
