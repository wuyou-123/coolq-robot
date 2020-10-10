package com.wuyou.robot.listeners;

import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.MostDIYType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.JSONUtils;
import com.wuyou.utils.CQ;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.RobotUtils;
import com.wuyou.utils.SenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class GroupOtherListeners {

//    int setuNum = 0;


//    @Listen(MsgGetTypes.groupMsg)
//    @Filter(diyFilter = "boot", value = {"点赞", "名片赞"})
//    public void sendLike(GroupMsg msg, MsgSender sender) {
//        String fromGroup = msg.getGroup();
//        String fromQQ = msg.getQQ();
//        try {
//            boolean isSucc = sender.SENDER.sendLike(fromQQ, 10);
//            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + (isSucc ? "已点赞" : "点赞失败"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "点赞失败");
//        }
//    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = ".*百度.*", at = true)
    public void baiduSearch(GroupMsg msg, MsgSender sender) throws UnsupportedEncodingException {
        String message = msg.getMsg();
        String search = message.substring(message.indexOf("百度") + 2).trim();
        if (search.length() > 0) {
            SenderUtil.sendGroupMsg(sender, msg.getGroup(),
                    "百度搜索 [" + search + "]：https://baidu.com/s?word=" + URLEncoder.encode(search, "UTF-8"));
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot", value = "呼叫龙王")
    public void findDargon(GroupMsg msg, MsgSender sender) throws IOException {
        String fromGroup = msg.getGroup();
        Map<String, String> cookies = RobotUtils.getCookies(sender);
        String bkn = RobotUtils.getBkn(sender);
        String url = "http://qun.qq.com/interactive/honorlist?gc=" + fromGroup + "&type=1&_wv=3&_wwv=129";
        String body = Jsoup.connect(url).ignoreContentType(true).cookies(cookies).get().html();
        String jsonStr = body.substring(body.indexOf("__INITIAL_STATE__=") + 18);
        jsonStr = jsonStr.substring(0, jsonStr.indexOf("</script>"));
        JSONObject json = JSONUtils.toJsonObject(jsonStr);
        JSONObject currentTalkative = json.getJSONObject("currentTalkative");
        if (currentTalkative != null) {
            String qq = currentTalkative.getString("uin");
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(qq));
        } else {
            SenderUtil.sendGroupMsg(sender, fromGroup, "当前暂无龙王");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "menu"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void sendMenu(GroupMsg msg, MsgSender sender) {
        SenderUtil.sendGroupMsg(sender, msg.getGroup(), "http://wuyourj.club/menu.html");
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "setu"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void sendSetu(GroupMsg msg, MsgSender sender) {
//        System.out.println("任务数: " + setuNum);
//        setuNum++;
//        if (setuNum > 5) {
//            try {
//                SenderUtil.sendGroupMsg(sender, msg.getGroup(), "任务太多啦!人家忙不过来啦~~");
//            } catch (Exception ignored) {
//            }
//            setuNum--;
//            return;
//        }
//        new Thread(() -> {
//            boolean r18 = msg.getMsg().toLowerCase().contains("r18");
//            long start = System.currentTimeMillis();
//            String CQCode;
//            System.out.println("开始获取图片");
//            try {
//                CQCode = r18 ? GlobalVariable.setuR18Queue.poll() : GlobalVariable.setuQueue.poll();
//                if (CQCode == null) {
//                    CQCode = r18 ? GlobalVariable.setuR18Queue.poll() : GlobalVariable.setuQueue.poll();
//                    if (CQCode == null) {
//                        System.out.println("获取到null");
//                        SenderUtil.sendGroupMsg(sender, msg.getGroup(), "色图已经发完啦,请稍等片刻~");
//                        return;
//                    }
//                }
//                System.out.println("获取到的CQCode" + CQCode);
//                if (CQCode.equals("0000")) {
//                    SenderUtil.sendGroupMsg(sender, msg.getGroup(), "今天的色图已经发完啦,明天再来吧~");
//                    return;
//                }
//                System.out.println("获取到的图片: " + CQCode);
//                System.out.println("图片CQ码: " + CQCode);
//                SenderUtil.sendGroupMsg(sender, msg.getGroup(), CQCode);
//                System.out.println("发送成功,耗时: " + (System.currentTimeMillis() - start));
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                setuNum--;
//            }
//        }).start();
        SenderUtil.sendGroupMsg(sender, msg.getGroup(), "涩图功能关闭啦,可以去http://wuyourj.club/apk/lolicon.apk下载app使用哦~");

    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "setuImage"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void sendSetuImage(GroupMsg msg, MsgSender sender) {
        sendSetu(msg, sender);
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "autistic1"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void autistic1(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
            sender.SETTER.setGroupBan(fromGroup, fromQQ, 1800);
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "自闭成功" + CQ.getFace("178"));
        } else {
            SenderUtil.sendGroupMsg(sender, fromGroup,
                    CQ.at(fromQQ) + "自闭失败,我没有禁言你的权限" + CQ.getFace("174"));
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "autistic2"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void autistic2(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String fromQQ = msg.getQQ();
        String message = msg.getMsg();
        String sub = message.substring(message.indexOf("领取套餐") + 4);
        String end = sub.trim();
        if ("".equals(end)) {
            Random r = new Random();
            int time = r.nextInt(30) + 1;
            if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
                sender.SETTER.setGroupBan(fromGroup, fromQQ, time * 60);
                SenderUtil.sendGroupMsg(sender, fromGroup,
                        CQ.at(fromQQ) + "恭喜领取了" + time + "分钟套餐" + CQ.getFace("178"));
            } else {
                SenderUtil.sendGroupMsg(sender, fromGroup,
                        CQ.at(fromQQ) + "领取失败,我没有禁言你的权限" + CQ.getFace("201"));
            }
            return;
        }
        try {
            if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
                String timeStr = sub.trim();
                int times = Integer.parseInt(timeStr);
                if (times > 1440 * 30) {
                    SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "领取失败,时间不能超过30天!");
                    return;
                }
                int time = times * 60;
                timeStr = getTime(time);
                sender.SETTER.setGroupBan(fromGroup, fromQQ, times * 60);
                SenderUtil.sendGroupMsg(sender, fromGroup,
                        CQ.at(fromQQ) + "恭喜领取了"
                                + timeStr.replace("天0小时", "天").replace("小时0分钟", "小时").replace("分钟0秒", "分钟") + "的套餐"
                                + CQ.getFace("201"));
            } else {
                SenderUtil.sendGroupMsg(sender, fromGroup,
                        CQ.at(fromQQ) + "领取失败,我没有禁言你的权限" + CQ.getFace("174"));
            }
        } catch (Exception e) {
            SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(fromQQ) + "领取失败,指令不合法");
        }
    }

    @NotNull
    static String getTime(int time) {
        String timeStr;
        if (time > 1440 * 60) {
            timeStr = time / 1440 / 60 + "天" + (time / 60) % 1440 / 60 + "小时" + (time / 60) % 1440 % 60 + "分钟";
        } else if (time > 60 * 60) {
            timeStr = time / 60 / 60 + "小时" + time / 60 % 60 + "分钟";
        } else {
            timeStr = time / 60 + "分钟" + time % 60 + "秒";
        }
        return timeStr;
    }

}
