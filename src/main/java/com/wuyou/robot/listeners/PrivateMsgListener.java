package com.wuyou.robot.listeners;

import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.robot.BootClass;
import com.wuyou.service.AllBlackService;
import com.wuyou.service.ClearService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GlobalVariable;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.results.GroupFullInfo;
import love.forte.simbot.api.message.results.GroupList;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.message.results.GroupOwner;
import love.forte.simbot.api.sender.MsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Autowired
    private BootClass boot;

    @OnPrivate
    public void testPrivateMsg(PrivateMsg msg, MsgSender sender) {
        String qq = msg.getAccountInfo().getAccountCode();
        String message = msg.getMsg();
        if (!"1097810498".equals(qq)) {
            return;
        }
        if (message.startsWith("点歌 ")) {
            String music = message.substring(3);
            System.out.println(music);

        }
        if (message.startsWith("重启斗地主")) {
            boot.initLandlords();
        }
        if ("a".equals(message)) {
            for (int i = 213; i < 300; i++) {
//                SenderUtil.sendPrivateMsg(qq, "id: " + i +"  "+ CQ.getFace(i + "").toString());
            }
        }
    }

    @OnPrivate
    @Filter(value = "点歌 .*")
    public void music(PrivateMsg msg, MsgSender sender) {
        String qq = msg.getAccountInfo().getAccountCode();
        String message = msg.getMsg();
        System.out.println("点歌");
        String music = message.trim().substring(3);
//        SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(),CQ.getMusic(music).toString());
        sender.SENDER.sendPrivateMsg(qq, CQ.getMusic(music).toString());
    }


    @OnPrivate
    public void privateMsg(PrivateMsg msg, MsgSender sender) {
//        String qq = msg.getAccountInfo().getAccountCode();
//        String message = msg.getMsg();
//        System.out.println(message);
//        if (GlobalVariable.ADMINISTRATOR.contains(qq)) {
//            try {
//                if (message.indexOf("给") == 0 && message.contains("发送消息")) {
//                    // 发送消息
//                    String toQQ = message.substring(1, message.indexOf("发送消息"));
//                    String toMessage = message.substring(message.indexOf("发送消息") + 4);
//                    sender.SENDER.sendPrivateMsg(toQQ, toMessage);
//                    sender.SENDER.sendPrivateMsg(qq, "已将[" + toMessage + "]发送给[" + toQQ + "]("
//                            + sender.GETTER.getFriendInfo(toQQ).getAccountInfo().getAccountRemarkOrNickname() + ")");
//                }
//            } catch (Exception e) {
//                sender.SENDER.sendPrivateMsg(qq, "出现异常\n" + e.getMessage());
//            }
//            return;
//        }
//        if (msg.getPrivateMsgType() == PrivateMsg.Type.SYS) {
//            return;
//        }
/////     收到他人发送的消息, 转发至自己
//        String adminQQ = "1097810498";
//        sender.SENDER.sendPrivateMsg(adminQQ,
//                "收到来自[" + qq + "](" + sender.GETTER.getFriendInfo(qq).getAccountInfo().getAccountRemarkOrNickname() + ")的一条消息");
//        sender.SENDER.sendPrivateMsg(adminQQ, msg.getMsg());
//        List<Neko> faces = CQ.getKq(message, "face");
//        for (Neko neko : faces) {
//            String str = FaceEnum.getString(neko.get("id"));
//            message = message.replace(neko, str);
//        }
//        JSONObject json = RequestUtil.aiChat(message, qq);
//        if (json.getInteger("code") == 200) {
//            String reply = json.getJSONArray("newslist").getJSONObject(0).getString("reply");
//            if (!reply.isEmpty()) {
//                sender.SENDER.sendPrivateMsg(qq, reply);
//                sender.SENDER.sendPrivateMsg("1097810498", "向[" + qq + "](" + sender.GETTER.getFriendInfo(qq).getAccountInfo().getAccountRemarkOrNickname() + ")发送消息: " + reply);
//                return;
//            }
//        }
//        System.out.println("请求错误");
//        sender.SENDER.sendPrivateMsg("1097810498", "聊天接口调用失败! QQ号: " + qq + ", 请求消息: " + message);

    }

    @OnPrivate
    public void cancelGroupBan(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        if (GlobalVariable.ADMINISTRATOR.contains(msg.getAccountInfo().getAccountCode()) && message.startsWith("解禁群")) {
            String group = message.substring(3);
            if (sender.SETTER.setGroupBan(group, msg.getAccountInfo().getAccountCode(), 0).get()) {
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "解禁成功");
            } else {
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "解禁失败");
            }
        }
    }

    @OnPrivate
    public void leaveGroupBan(PrivateMsg msg, MsgSender sender) {
        try {
            String message = msg.getMsg();
            if (GlobalVariable.ADMINISTRATOR.contains(msg.getAccountInfo().getAccountCode()) && message.startsWith("退群")) {
                String group = message.substring(2);
                if (sender.GETTER.getMemberInfo(group, msg.getBotInfo().getBotCode()).getPermission().isOwner()) {
                    sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "退出失败,不能退出我的群");
                    return;
                }
                sender.SETTER.setGroupQuit(group, false);
                clearService.clearAllData(group);
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "退出成功");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("-16")) {
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "ID：3804 错误：帐号不在群内或网络错误，无法退出/解散该群 (-16)");
            } else {
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), e.getMessage());
            }
        }
    }

    @OnPrivate
    public void cancelGroupAllBan(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        if (GlobalVariable.ADMINISTRATOR.contains(msg.getAccountInfo().getAccountCode()) && message.startsWith("全体解禁")) {
            String group = message.substring(4);
            if (sender.SETTER.setGroupWholeBan(group, false).get()) {
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "解禁成功");
            } else {
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "解禁失败");
            }
        }
    }

    @OnPrivate
    @Filter("群列表")
    public void groupList(PrivateMsg msg, MsgSender sender) {
        if (GlobalVariable.ADMINISTRATOR.contains(msg.getAccountInfo().getAccountCode())) {
            GlobalVariable.THREAD_POOL.execute(() -> {
                // 发送群列表
                GroupList groupEntities = sender.GETTER.getGroupList();
                StringBuilder groupInfo = new StringBuilder("群列表:\n");
                AtomicInteger num = new AtomicInteger(0);
                groupEntities.forEach(group -> {
                    String groupCode = group.getGroupCode();
                    GroupOwner owner = sender.GETTER.getGroupInfo(groupCode).getOwner();
                    groupInfo.append(num.getAndIncrement() + 1).append(". ").append(group.getGroupName()).append("(").append(group.getGroupCode()).append(")  [").append(sender.GETTER.getMemberInfo(group.getGroupCode(), sender.GETTER.getBotInfo().getBotCode()).getPermission()).append("]\n");
                    groupInfo.append("\t群主信息: ").append(owner.getAccountInfo().getAccountRemarkOrNickname()).append("(").append(owner.getAccountInfo().getAccountCode()).append(")\n\n");
                    if (num.get() % 10 == 0) {
                        groupInfo.append("\n\n\n");
                    }
                });
//                List<GroupEntity> groupList = GroupUtils.getGroupList(sender);
//                StringBuilder groupInfo = new StringBuilder("群列表:\n");
//                AtomicInteger num = new AtomicInteger(0);
//                groupList.forEach(group -> {
//                    System.out.println(group);
//                    groupInfo.append(num.getAndIncrement() + 1).append(". ").append(group.getGroupName()).append("(").append(group.getUin()).append(")  ").append(group.getRole()).append("\n");
//                    groupInfo.append("\t群主信息: ").append(group.getOwner().getNick()).append("(").append(group.getOwner().getUin()).append(")\n\n");
//                    if (num.get() % 10 == 0) {
//                        groupInfo.append("\n\n\n");
//                    }
//                });
                String[] messageArray = groupInfo.toString().split("\n\n\n");
                for (String message : messageArray) {
                    sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), message.trim());
                }
            });

        }
    }

    @OnPrivate
    public void groupDetail(PrivateMsg msg, MsgSender sender) {
        try {
            String message = msg.getMsg();
            if (GlobalVariable.ADMINISTRATOR.contains(msg.getAccountInfo().getAccountCode()) && message.startsWith("群信息")) {
                String group = message.substring(3);
                GroupFullInfo groupInfo = sender.GETTER.getGroupInfo(group);
                StringBuilder resultMessage = new StringBuilder();
                resultMessage.append("群号: ").append(groupInfo.getGroupCode());
                resultMessage.append("\n群名称: ").append(groupInfo.getGroupName());
                sender.GETTER.getGroupMemberList(group);
                resultMessage.append("\n群介绍: ").append(groupInfo.getFullIntroduction());
                resultMessage.append("\n群主QQ: ").append(groupInfo.getOwner().getAccountInfo().getAccountCode());
                sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), resultMessage.toString());
            }
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "出现异常\n" + e.getMessage());
        }
    }

///    @OnPrivate
///    public void restartServer(PrivateMsg msg, MsgSender sender) {
///        String message = msg.getMsg();
///        if (GlobalVariable.ADMINISTRATOR.contains(msg.getAccountInfo().getAccountCode()) && "重启".equals(message)) {
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

    @OnPrivate
    public void setAllBlackUser(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getAccountInfo().getAccountCode();
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

    @OnPrivate
    public void cancelAllBlackUser(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getAccountInfo().getAccountCode();
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

    @OnPrivate
    public void setAllBlackGroup(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getAccountInfo().getAccountCode();
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
                str.append(setBlackGroups(msg.getBotInfo().getBotCode(), list, sender));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sender.SENDER.sendPrivateMsg(qq, "指令不合法");
                return;
            }
            sender.SENDER.sendPrivateMsg(qq, str.toString());
        }
    }

    @OnPrivate
    public void cancelAllBlackGroup(PrivateMsg msg, MsgSender sender) {
        String message = msg.getMsg();
        String qq = msg.getAccountInfo().getAccountCode();
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

    @OnPrivate
    @Filter(".*黑名单")
    public void allBlackUserList(PrivateMsg msg, MsgSender sender) {
        String qq = msg.getAccountInfo().getAccountCode();
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
            if (sender.GETTER.getBotInfo().getBotCode().equals(user + "")) {
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
            if (sender.GETTER.getBotInfo().getBotCode().equals(user + "")) {
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
                info = sender.GETTER.getMemberInfo(user, thisQQ);
                if (info.getPermission().isOwner()) {
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