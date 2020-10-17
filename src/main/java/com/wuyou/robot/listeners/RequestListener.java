package com.wuyou.robot.listeners;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.FriendAddRequest;
import com.forte.qqrobot.beans.messages.msgget.GroupAddRequest;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberIncrease;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.GroupAddRequestType;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.messages.types.PowerType;
import com.forte.qqrobot.beans.messages.types.SexType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.sender.senderlist.SenderGetList;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.wuyou.service.AllBlackService;
import com.wuyou.service.BlackUserService;
import com.wuyou.service.StatService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GetLevelUtils;
import com.wuyou.utils.SenderUtil;

import java.util.ArrayList;
import java.util.List;

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

    String adminqq = "1097810498";

    @Listen(MsgGetTypes.friendAddRequest)
    public void friendAddRequest(FriendAddRequest request, MsgSender sender) {
        // TODO: 好友添加的请求
        sender.SETTER.setFriendAddRequest(request, "", true);
        sender.SENDER.sendPrivateMsg(adminqq,
                "已经添加[" + request.getQQ() + "](" + sender.getPersonInfoByCode(request.getQQ()).getRemarkOrNickname() + ")为好友,验证消息为"
                        + ("".equals(request.getMsg()) ? "空" : ": " + request.getMsg()));
    }

    @Listen(MsgGetTypes.groupAddRequest)
    public void groupAddRequest(GroupAddRequest request, MsgSender sender) {
        // TODO: 群添加请求
        String fromGroup = request.getGroup();
        String qq = request.getQQ();
        SenderGetList msg = sender.GETTER;

        List<String> list = getAllBlackUser(fromGroup);
        // 判断是不是黑名单里的成员
        if (list.contains(qq)) {
            sender.SETTER.setGroupAddRequest(request, false, "");
            sender.SENDER.sendPrivateMsg(adminqq, "已经拒绝黑名单成员[" + request.getQQ() + "]("
                    + sender.getPersonInfoByCode(qq).getRemarkOrNickname() + ")加入群[" + request.getGroup() + "]("
                    + msg.getGroupInfo(request.getGroup()).getName() + ")," + ("".equals(request.getMsg()) ? "附加消息为空"
                    : request.getMsg().contains("邀请人") ? request.getMsg() : "附加消息为: " + request.getMsg()));
            SenderUtil.sendGroupMsg(sender, fromGroup, "已经拒绝黑名单成员[" + request.getQQ() + "]("
                    + sender.getPersonInfoByCode(qq).getRemarkOrNickname() + ")加入群, " + ("".equals(request.getMsg()) ? "附加消息为空"
                    : request.getMsg().contains("邀请人") ? request.getMsg() : "附加消息为: " + request.getMsg()));
            return;
        }

        if (request.getRequestType() == GroupAddRequestType.INVITE) {
            // 邀请机器人进群
            List<String> allBlackGroups = allBlackUserService.getAllBlack(2);
            if (allBlackGroups.contains(request.getGroup())) {
                sender.SETTER.setGroupAddRequest(request, false, "");
                sender.SENDER.sendPrivateMsg(adminqq,
                        "[" + request.getQQ() + "](" + sender.getPersonInfoByCode(qq).getRemarkOrNickname() + ")邀请我加入群["
                                + request.getGroup() + "](" + sender.getGroupInfoByCode(request.getGroup()).getName()
                                + "),但是由于此群在黑名单内被我拒绝了");
                return;
            }
            sender.SETTER.setGroupAddRequest(request, true, "");
            sender.SENDER.sendPrivateMsg(adminqq,
                    "[" + request.getQQ() + "](" + sender.getPersonInfoByCode(qq).getRemarkOrNickname() + ")已邀请我加入群["
                            + request.getGroup() + "](" + msg.getGroupInfo(request.getGroup()).getName() + ")");
        }
//        else {
//            // 同意群申请
//            // 邀请他人进群
////			sender.SETTER.setGroupAddRequest(request, true, "");
////			sender.SENDER.sendPrivateMsg(adminQQ, "已经同意[" + request.getQQ() + "]("
////					+ sender.getPersonInfoByCode(qq).getName() + ")加入群[" + request.getGroup() + "]("
////					+ msg.getGroupInfo(request.getGroup()).getName() + ")," + (request.getMsg().equals("") ? "附加消息为空"
////							: request.getMsg().contains("邀请人") ? request.getMsg() : "附加消息为: " + request.getMsg()));
//        }
    }

    @Listen(MsgGetTypes.groupMemberIncrease)
    public void groupMemberIncrease(GroupMemberIncrease increase, MsgSender sender) {
        // TODO: 新群员进群
        SenderGetList msg = sender.GETTER;
        CQCodeUtil util = CQCodeUtil.build();
        String fromGroup = increase.getGroup();
        String beingOperateqq = increase.getBeOperatedQQ();
        int level = GetLevelUtils.getLevel(beingOperateqq);
        System.out.println("等级: " + level);
        String loginqq = increase.getThisCode();
        if (loginqq.equals(beingOperateqq)) {
            // 如果新成员是自己
            List<String> allBlackUsers = allBlackUserService.getAllBlack(2);
            if (allBlackUsers.contains(fromGroup)) {
                sender.SENDER.sendPrivateMsg(adminqq,
                        "有人邀请我加入群[" + fromGroup + "](" + msg.getGroupInfo(fromGroup).getName() + "),但是由于此群在黑名单内我退出了");
                sender.SETTER.setGroupLeave(fromGroup);
            }
            return;
        }
        try {
            List<String> list = getAllBlackUser(fromGroup);
            // 判断是不是黑名单里的成员
            if (list.contains(beingOperateqq)) {
                GroupMemberInfo beingOperateMember = msg.getGroupMemberInfo(fromGroup, beingOperateqq);
                if (msg.getGroupMemberInfo(fromGroup, loginqq).getPowerType() != PowerType.MEMBER) {
                    sender.SETTER.setGroupMemberKick(fromGroup, beingOperateqq, true);
                    SenderUtil.sendGroupMsg(sender, fromGroup,
                            "发现黑名单成员[" + beingOperateqq + "](" + beingOperateMember.getRemarkOrNickname() + ")入群,已将他移除此群");
                } else {
                    SenderUtil.sendGroupMsg(sender, fromGroup, "发现黑名单成员[" + beingOperateqq + "]("
                            + beingOperateMember.getRemarkOrNickname() + ")入群,但是我没有权限将他移除此群");
                }
                return;

            }
            // 新人不是黑名单成员
            if (statService.getStat(fromGroup) == 1) {
                GroupMemberInfo beingOperateMember = msg.getGroupMemberInfo(fromGroup, beingOperateqq);
                String end = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89";
                String welcomeMessage = "欢迎" + (beingOperateMember.getSex() == SexType.MALE ? "新来的小哥哥"
                        : beingOperateMember.getSex() == SexType.FEMALE ? "新来的小姐姐" : "新人") +
                        (level <= 3 && level > 0 ? "，你的等级小于三级,该不会是谁的小号吧"
                                : level <= 5 ? "，你的等级为" + level + "级,有一点点低哦"
                                : level <= 10 ? "，你的等级为" + level + "级,有一点低哦" : end);
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(beingOperateqq) + welcomeMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sender.SENDER.sendPrivateMsg(adminqq, "出现异常");
            sender.SENDER.sendPrivateMsg(adminqq, e.getMessage());
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

    @Listen(MsgGetTypes.groupMsg)
    @Filter("群活跃信息")
    public void testListen2(GroupMsg msg, MsgSender sender) {
        System.out.println("发送群活跃信息");
        SenderUtil.sendGroupMsg(sender, msg.getGroup(),
                "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + msg.getGroup());
    }

//	var m = new HashMap<String, String[]>();
//
//	/**
//	 * 测试记录上下文
//	 */
//	@Listen(MsgGetTypes.privateMsg)
//	public void test(PrivateMsg msg, MsgSender sender) {
//		String groupQQ = "123456";
//		if (m.get(groupQQ) == null)
//			m.put(groupQQ, new String[] { "", "" });
//		String message = msg.getMsg();
//		if (m.get(groupQQ) == null) {
//			m.get(groupQQ)[0] = message;
//			return;
//		}
//		System.out.println("上一条消息: " + m.get(groupQQ)[0]);
//		m.get(groupQQ)[1] = message;
//		if (!m.get(groupQQ)[0].equals(m.get(groupQQ)[1]))
//			m.get(groupQQ)[0] = m.get(groupQQ)[1];
//		System.out.println("此条消息: " + m.get(groupQQ)[1]);
//	}
}
