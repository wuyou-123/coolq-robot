package com.wuyou.robot.listeners;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.MostDIYType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.BanMessageService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年3月13日
 */
@Beans
public class GroupBanMessageListener {

    @Depend
    BanMessageService service;

    List<String> administrator = new ArrayList<String>();

    public GroupBanMessageListener() {
        administrator.add("1097810498");
        administrator.add("1041025733");
        administrator.add("2973617637");
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = {"禁言关键词", "关键词列表"}, diyFilter = "boot")
    public void sendBanMessages(GroupMsg msg, MsgSender sender) {
        System.out.println("发送禁言关键词列表");
        String fromGroup = msg.getGroup();
        List<String> list = service.getAllByGroupId(fromGroup);
        if (list.size() == 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, "暂无回复记录");
            return;
        }
        StringBuilder mes = new StringBuilder(msg.getMsg() + ":");
        for (String str : list) {
            mes.append("\n\t关键词: \"" + str + "\"");
        }
        SenderUtil.sendGroupMsg(sender, fromGroup, mes.toString().trim());
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "sendBanMessage"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void sendMessage(GroupMsg msg, MsgSender sender) {

        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        String message = msg.getMsg();
        List<String> list = service.getAllByGroupId(fromGroup);

        if (list.size() == 0) {
            return;
        }
        int num = 0;
        if (message.contains("抽") || message.contains("chou")) {
            num++;
        }
        if (message.contains("奖") || message.contains("jiang")) {
            num++;
        }
        if (list.contains("抽奖") && (num == 2)) {
            luck(msg, sender);
            return;
        }
        Random ran = new Random();
        int a = ran.nextInt(30) + 2;

        for (String banMessage : list) {
            if (message.contains(banMessage)) {
                if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
                    SenderUtil.sendGroupMsg(sender, fromGroup,
                            CQ.at(fromQQ) + "发送关键词[" + banMessage + "]！ 抽中禁言" + a / 2 + "分");
                    sender.SETTER.setGroupBan(fromGroup, fromQQ, (a / 2) * 60);
                    continue;
                } else {
                    SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "发送关键词[" + banMessage + "]， 抽中禁言" + a / 2
                            + "分，可是我没有禁言你的权限" + CQCodeUtil.build().getCQCode_Face("174"));
                    continue;
                }
            }
        }

    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "addBanMessage"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void addMessage(GroupMsg msg, MsgSender sender) {
        String mess = msg.getMsg();
        String group = msg.getGroup();
        String qq = msg.getQQ();
        if (PowerUtils.getPowerType(group, qq, sender) > 1) {
            System.out.println("执行添加关键词代码");
            String message = mess.substring(mess.indexOf("添加关键词") + 5).trim();
            if (message.equals("")) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "添加失败: 内容不完整");
                return;
            }
            try {
                service.addBanMessage(group, message);
                if (message.equals("抽奖")) {
                    SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n添加成功: \n\t\t已开启抽奖功能!");
                } else {
                    SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n添加成功: \n\t\t消息内容: " + message);
                }
            } catch (ObjectExistedException e) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n更改失败: \n此条关键词已存在");
                return;
            }
        } else {
            SenderUtil.sendGroupMsg(sender, group, "添加失败,你不是我的管理员!");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "removeBanMessage"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void removeMessage(GroupMsg msg, MsgSender sender) {
        String mess = msg.getMsg();
        String group = msg.getGroup();
        String qq = msg.getQQ();
        if (PowerUtils.getPowerType(group, qq, sender) > 1) {
            System.out.println("执行删除关键词代码");
            String message = mess.substring(mess.indexOf("删除关键词") + 5).trim();
            if (message.equals("")) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "删除失败: 内容不完整");
                return;
            }
            try {
                service.removeBanMessage(group, message);
                if (message.equals("抽奖")) {
                    SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n删除成功: \n\t\t已关闭抽奖功能!");
                } else {
                    SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n删除成功: \n\t\t关键词内容: " + message);
                }
            } catch (ObjectNotFoundException e) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n删除失败: \n没找到此条消息");
                return;
            }
        } else {
            SenderUtil.sendGroupMsg(sender, group, "删除失败,你不是我的管理员!");
        }
    }

    /**
     *
     */
    private void luck(GroupMsg msg, MsgSender sender) {
        System.out.println("执行抽奖代码");
        Random ran = new Random();
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();

        String[] context = CQ.context.get(fromGroup);
        int probability = 100;
        int max = 10;

        if (context != null && (context[2].contains("轮空") || context[3].contains("轮空"))) {
            probability = 150;
            max = 5;
        }
        System.out.println("概率: " + probability);
        int a = ran.nextInt(probability);
        int min = a % max == 0 ? 1 : a % max;
        if (a <= 90) {
            if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "抽中禁言" + min + "分");
                sender.SETTER.setGroupBan(fromGroup, fromQQ, min * 60);
            } else {
                SenderUtil.sendGroupMsg(sender, fromGroup,
                        CQ.at(fromQQ) + "抽中禁言" + min + "分，可是我没有禁言你的权限" + CQCodeUtil.build().getCQCode_Face("174"));
            }
        } else {
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "恭喜轮空");
        }
    }

}
