package com.wuyou.robot.listeners;

import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.result.GroupInfo;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.JSONUtils;
import com.simplerobot.modules.utils.KQCode;
import com.wuyou.entity.GroupEntity;
import com.wuyou.enums.FaceEnum;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.AllBlackService;
import com.wuyou.service.ClearService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.GroupUtils;
import com.wuyou.utils.HttpUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator<br>
 * 2020年3月13日
 */
@Beans
@Component
public class PrivateMsgListener {
    @Depend
    private ClearService clearService;
    @Depend
    private AllBlackService allBlackUserService;

///    @Value("${command}")
///    private String commandStr;

    @Listen(MsgGetTypes.privateMsg)
    public void privateMsg(PrivateMsg msg, MsgSender sender) {
        String qq = msg.getQQ();
        String message = msg.getMsg();
        System.out.println(message);
        if (GlobalVariable.ADMINISTRATOR.contains(qq)) {
            try {
                if (message.indexOf("给") == 0 && message.contains("发送消息")) {
                    // 发送消息
                    String toQQ = message.substring(1, message.indexOf("发送消息"));
                    String toMessage = message.substring(message.indexOf("发送消息") + 4);
                    sender.SENDER.sendPrivateMsg(toQQ, toMessage);
                    sender.SENDER.sendPrivateMsg(qq, "已将[" + toMessage + "]发送给[" + toQQ + "]("
                            + sender.getPersonInfoByCode(toQQ).getRemarkOrNickname() + ")");
                }
            } catch (Exception e) {
                sender.SENDER.sendPrivateMsg(qq, "出现异常\n" + e.getMessage());
            }
            return;
        }
        if (msg.getType().isFromSystem()) {
            return;
        }
///     收到他人发送的消息, 转发至自己
        String adminQQ = "1097810498";
        sender.SENDER.sendPrivateMsg(adminQQ,
                "收到来自[" + qq + "](" + sender.getPersonInfoByCode(qq).getRemarkOrNickname() + ")的一条消息");
        sender.SENDER.sendPrivateMsg(adminQQ, msg.getMsg());
        List<KQCode> faces = CQ.getKq(message, "face");
        for (KQCode KQCode : faces) {
            String str = FaceEnum.getString(KQCode.get("id"));
            message = message.replace(KQCode, str);
        }
        String url = "http://api.tianapi.com/txapi/tuling/index";
        Map<String, String> params = new HashMap<>(4);
        params.put("key", "9845b4e0442683f1f8ab813c35180fc5");
        params.put("question", message);
        params.put("user", qq);
        String web = HttpUtils.get(url, params, null).getResponse();
        System.out.println("请求: " + message);
        System.out.println("返回值: " + web);
        JSONObject json = JSONUtils.toJsonObject(web);
        if (json.getInteger("code") == 200) {
            String reply = json.getJSONArray("newslist").getJSONObject(0).getString("reply");
            if (!reply.isEmpty()) {
                sender.SENDER.sendPrivateMsg(qq, reply);
                sender.SENDER.sendPrivateMsg("1097810498", "向[" + qq + "](" + sender.getPersonInfoByCode(qq).getRemarkOrNickname() + ")发送消息: " + reply);
                return;
            }
        }
        System.out.println("请求错误");
        sender.SENDER.sendPrivateMsg("1097810498", "聊天接口调用失败! QQ号: " + qq + ", 请求消息: " + message);

    }

    @Listen(MsgGetTypes.privateMsg)
    public void cancelGroupBan(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        if (GlobalVariable.ADMINISTRATOR.contains(msg.getQQ()) && message.startsWith("解禁群")) {
            String group = message.substring(3);
            if (sender.SETTER.setGroupBan(group, msg.getQQ(), 0)) {
                sender.SENDER.sendPrivateMsg(msg.getQQ(), "解禁成功");
            } else {
                sender.SENDER.sendPrivateMsg(msg.getQQ(), "解禁失败");
            }
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    public void leaveGroupBan(PrivateMsg msg, MsgSender sender) {
        try {
            String message = msg.getMsg();
            if (GlobalVariable.ADMINISTRATOR.contains(msg.getQQ()) && message.startsWith("退群")) {
                String group = message.substring(2);
                if (sender.GETTER.getGroupMemberInfo(group, msg.getThisCode()).getPowerType().isOwner()) {
                    sender.SENDER.sendPrivateMsg(msg.getQQ(), "退出失败,不能退出我的群");
                    return;
                }
                sender.SETTER.setGroupLeave(group);
                clearService.clearAllData(group);
                sender.SENDER.sendPrivateMsg(msg.getQQ(), "退出成功");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("-16")) {
                sender.SENDER.sendPrivateMsg(msg.getQQ(), "ID：3804 错误：帐号不在群内或网络错误，无法退出/解散该群 (-16)");
            } else {
                sender.SENDER.sendPrivateMsg(msg.getQQ(), e.getMessage());
            }
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    public void cancelGroupAllBan(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        if (GlobalVariable.ADMINISTRATOR.contains(msg.getQQ()) && message.startsWith("全体解禁")) {
            String group = message.substring(4);
            if (sender.SETTER.setGroupWholeBan(group, false)) {
                sender.SENDER.sendPrivateMsg(msg.getQQ(), "解禁成功");
            } else {
                sender.SENDER.sendPrivateMsg(msg.getQQ(), "解禁失败");
            }
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    @Filter("群列表")
    public void groupList(PrivateMsg msg, MsgSender sender) {
        if (GlobalVariable.ADMINISTRATOR.contains(msg.getQQ())) {
            GlobalVariable.THREAD_POOL.execute(() -> {
                // 发送群列表
                List<GroupEntity> groupList = GroupUtils.getGroupList(sender);
                System.out.println(groupList.size());
                StringBuilder groupInfo = new StringBuilder("群列表:\n");
                AtomicInteger num = new AtomicInteger(0);
                groupList.forEach(group -> {
                    System.out.println(group);
                    groupInfo.append(num.getAndIncrement() + 1).append(". ").append(group.getGroupName()).append("(").append(group.getUin()).append(")  ").append(group.getRole()).append("\n");
                    groupInfo.append("\t群主信息: ").append(group.getOwner().getNick()).append("(").append(group.getOwner().getUin()).append(")\n\n");
                    if (num.get() % 10 == 0) {
                        groupInfo.append("\n\n\n");
                    }
                });
                String[] messageArray = groupInfo.toString().split("\n\n\n");
                for (String message : messageArray) {
                    sender.SENDER.sendPrivateMsg(msg.getQQ(), message.trim());
                }
            });

        }
    }

    @Listen(MsgGetTypes.privateMsg)
    public void groupDetail(PrivateMsg msg, MsgSender sender) {
        try {
            String message = msg.getMsg();
            if (GlobalVariable.ADMINISTRATOR.contains(msg.getQQ()) && message.startsWith("群信息")) {
                String group = message.substring(3);
                GroupInfo groupInfo = sender.GETTER.getGroupInfo(group, true);
                if (groupInfo == null) {
                    System.out.println("没有找到群信息");
                    sender.SENDER.sendPrivateMsg(msg.getQQ(), "没有找到群信息");
                    return;
                }
                StringBuilder resultMessage = new StringBuilder();
                resultMessage.append("群号: ").append(groupInfo.getCode());
                resultMessage.append("\n群名称: ").append(groupInfo.getName());
                sender.GETTER.getGroupMemberList(group);
                resultMessage.append("\n群介绍: ").append(groupInfo.getCompleteIntro());
                resultMessage.append("\n群主QQ: ").append(groupInfo.getOwnerQQ());
                sender.SENDER.sendPrivateMsg(msg.getQQ(), resultMessage.toString());
            }
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(msg.getQQ(), "出现异常\n" + e.getMessage());
        }
    }

///    @Listen(MsgGetTypes.privateMsg)
///    public void restartServer(PrivateMsg msg, MsgSender sender) {
///        String message = msg.getMsg();
///        if (GlobalVariable.ADMINISTRATOR.contains(msg.getQQ()) && "重启".equals(message)) {
///            List<String> sb = new ArrayList<>();
///            try {
///                System.out.println(commandStr);
///                if (commandStr == null) {
///                    return;
///                }
///                Process ps = Runtime.getRuntime().exec(commandStr);
///
///                BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
///                String line;
///                while ((line = br.readLine()) != null) {
///                    sb.add(line);
///                }
///                System.out.println(sb);
///            } catch (Exception e) {
///                sender.SENDER.sendPrivateMsg(adminQQ, "捕获到异常");
///                e.printStackTrace();
/////                System.exit(0);
///            }
///        }
///    }

    @Listen(MsgGetTypes.privateMsg)
    public void setAllBlackUser(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getQQ();
        if (GlobalVariable.ADMINISTRATOR.contains(qq) && message.startsWith("拉黑")) {
            StringBuilder str = new StringBuilder("添加黑名单:");
            try {
                Set<String> list = new HashSet<>();
                String[] users = message.substring(message.indexOf("拉黑") + 2).split(",");
                for (String user1 : users) {
                    for (String user2 : user1.split("，")) {
                        Long.parseLong(user2);
                        list.add(user2);
                    }
                }
                str.append(setBlackUsers(list, sender));
            } catch (NumberFormatException e) {
                sender.SENDER.sendPrivateMsg(qq, "指令不合法");
                return;
            }
            sender.SENDER.sendPrivateMsg(qq, str.toString());
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    public void cancelAllBlackUser(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getQQ();
        if (GlobalVariable.ADMINISTRATOR.contains(qq) && message.startsWith("取消拉黑")) {
            StringBuilder str = new StringBuilder("取消拉黑:");
            try {
                Set<String> list = new HashSet<>();
                String[] users = message.substring(message.indexOf("取消拉黑") + 4).split(",");
                for (String user1 : users) {
                    for (String user2 : user1.split("，")) {
                        Long.parseLong(user2);
                        list.add(user2);
                    }
                }
                str.append(cancelBlackUsers(list, sender));
            } catch (NumberFormatException e) {
                sender.SENDER.sendPrivateMsg(qq, "指令不合法");
                return;
            }
            sender.SENDER.sendPrivateMsg(qq, str.toString());
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    public void setAllBlackGroup(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getQQ();
        if (GlobalVariable.ADMINISTRATOR.contains(qq) && message.startsWith("群拉黑")) {
            StringBuilder str = new StringBuilder("添加群黑名单:");
            try {
                Set<String> list = new HashSet<>();
                String[] users = message.substring(message.indexOf("群拉黑") + 3).split(",");
                for (String user1 : users) {
                    for (String user2 : user1.split("，")) {
                        Long.parseLong(user2);
                        list.add(user2);
                    }
                }
                str.append(setBlackGroups(msg.getThisCode(), list, sender));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sender.SENDER.sendPrivateMsg(qq, "指令不合法");
                return;
            }
            sender.SENDER.sendPrivateMsg(qq, str.toString());
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    public void cancelAllBlackGroup(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getQQ();
        if (GlobalVariable.ADMINISTRATOR.contains(qq) && message.startsWith("取消群拉黑")) {
            StringBuilder str = new StringBuilder("取消群拉黑:");
            try {
                Set<String> list = new HashSet<>();
                String[] users = message.substring(message.indexOf("取消群拉黑") + 5).split(",");
                for (String user1 : users) {
                    for (String user2 : user1.split("，")) {
                        Long.parseLong(user2);
                        list.add(user2);
                    }
                }
                str.append(cancelBlackGroups(list, sender));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sender.SENDER.sendPrivateMsg(qq, "指令不合法");
                return;
            }
            sender.SENDER.sendPrivateMsg(qq, str.toString());
        }
    }

    @Listen(MsgGetTypes.privateMsg)
    @Filter(".*黑名单")
    public void allBlackUserList(PrivateMsg msg, MsgSender sender) {
        String qq = msg.getQQ();
        String message = msg.getMsg();
        int type = 0;
        if ("黑名单".equals(message)) {
            type = 1;
        } else if (("群黑名单".equals(message))) {
            type = 2;
        }
        if (type == 0) {
            return;
        }
        if (GlobalVariable.ADMINISTRATOR.contains(qq)) {
            StringBuilder str = new StringBuilder(type == 1 ? "黑名单:" : "群黑名单:");
            List<String> list = allBlackUserService.getAllBlack(type);
            if (list.size() == 0) {
                sender.SENDER.sendPrivateMsg(qq, type == 1 ? "黑名单为空" : "群黑名单为空");
                return;
            }
            for (String user : list) {
                str.append("\n\t\t").append(user);
            }
            sender.SENDER.sendPrivateMsg(qq, str.toString());
        }
    }

    private String setBlackUsers(Set<String> list, MsgSender sender) {
        StringBuilder str = new StringBuilder();
        for (String user : list) {
            if (GlobalVariable.ADMINISTRATOR.contains(user + "")) {
                str.append("\n\t\t添加黑名单失败: QQ:[").append(user).append("],你拉黑自己干嘛?");
                continue;
            }
            if (sender.GETTER.getLoginQQInfo().getQQ().equals(user + "")) {
                str.append("\n\t\t添加黑名单失败: QQ:[").append(user).append("],我这辈子不可能拉黑自己!");
                continue;
            }
            try {
                allBlackUserService.addAllBlack(1, user);
            } catch (ObjectExistedException e) {
                str.append("\n\t\t添加黑名单失败: QQ:[").append(user).append("]已存在");
                continue;
            }
            str.append("\n\t\t添加黑名单成功: QQ:[").append(user).append("]已添加");
        }
        return str.toString();
    }

    private String cancelBlackUsers(Set<String> list, MsgSender sender) {
        StringBuilder str = new StringBuilder();
        List<String> users = allBlackUserService.getAllBlack(1);
        if (users.size() == 0) {
            return "取消失败,黑名单为空";
        }
        for (String user : list) {
            if (GlobalVariable.ADMINISTRATOR.contains(user + "")) {
                str.append("\n\t\t移除黑名单失败: QQ:[").append(user).append("],你不在黑名单!");
                continue;
            }
            if (sender.GETTER.getLoginQQInfo().getQQ().equals(user + "")) {
                str.append("\n\t\t移除黑名单失败: QQ:[").append(user).append("],我怎么可能出现在黑名单!");
                continue;
            }
            try {
                allBlackUserService.removeAllBlack(1, user);
            } catch (ObjectNotFoundException e) {
                str.append("\n\t\t移除黑名单失败: QQ:[").append(user).append("]不在黑名单");
                continue;
            }
            str.append("\n\t\t移除黑名单成功: QQ:[").append(user).append("]已移除");
        }
        return str.toString();
    }

    private String setBlackGroups(String thisQQ, Set<String> list, MsgSender sender) {
        StringBuilder str = new StringBuilder();
        for (String user : list) {
            GroupMemberInfo info;
            try {
                info = sender.GETTER.getGroupMemberInfo(user, thisQQ);
                if (info.getPowerType().isOwner()) {
                    str.append("\n\t\t添加黑名单失败: 群:[").append(user).append("]失败,不能拉黑自己的群");
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                allBlackUserService.addAllBlack(2, user);
            } catch (ObjectExistedException e) {
                str.append("\n\t\t添加黑名单失败: 群:[").append(user).append("]已存在");
                continue;
            }
            str.append("\n\t\t添加黑名单成功: 群:[").append(user).append("]已添加");
        }
        return str.toString();
    }

    private String cancelBlackGroups(Set<String> list, MsgSender sender) {
        StringBuilder str = new StringBuilder();
        List<String> users = allBlackUserService.getAllBlack(2);
        if (users.size() == 0) {
            return "取消失败,黑名单为空";
        }
        for (String user : list) {
            try {
                allBlackUserService.removeAllBlack(2, user);
            } catch (ObjectNotFoundException e) {
                str.append("\n\t\t移除黑名单失败: 群:[").append(user).append("]不在黑名单");
                continue;
            }
            str.append("\n\t\t移除黑名单成功: 群:[").append(user).append("]已移除");
        }
        return str.toString();
    }

//	Map<String, String> m = new HashMap<String, String[]>();
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
//
//		System.out.println("上一条消息: " + m.get(groupQQ)[0]);
//		m.get(groupQQ)[1] = message;
//		if (!m.get(groupQQ)[0].equals(m.get(groupQQ)[1]))
//			m.get(groupQQ)[0] = m.get(groupQQ)[1];
//		System.out.println("此条消息: " + m.get(groupQQ)[1]);
//	}
}