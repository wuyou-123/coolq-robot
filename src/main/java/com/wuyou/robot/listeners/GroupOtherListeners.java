package com.wuyou.robot.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuyou.utils.*;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.filter.MostMatchType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Beans
public class GroupOtherListeners {

///    int setuNum = 0;


///    @OnGroup
///    @Filters(customFilter = "boot", value = {"点赞", "名片赞"})
///    public void sendLike(GroupMsg msg, MsgSender sender) {
///        String fromGroup = msg.getGroupInfo().getGroupCode();
///        String fromQQ = msg.getAccountInfo().getAccountCode();
///        try {
///            boolean isSucc = sender.SENDER.sendLike(fromQQ, 10);
///            SenderUtil.sendGroupMsg(fromGroup, CQ.at(fromQQ) + (isSucc ? "已点赞" : "点赞失败"));
///        } catch (Exception e) {
///            e.printStackTrace();
///            SenderUtil.sendGroupMsg(fromGroup, CQ.at(fromQQ) + "点赞失败");
///        }
///    }

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

    @OnGroup
    @Filters(value = {
            @Filter(value = "百度", matchType = MatchType.CONTAINS)
    }, customFilter = "boot", atBot = true)
    public void baiduSearch(GroupMsg msg, MsgSender sender) throws UnsupportedEncodingException {
        String message = msg.getMsg();
        String search = message.substring(message.indexOf("百度") + 2).trim();
        if (search.length() > 0) {
            SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(),
                    "百度搜索 [" + search + "]：https://baidu.com/s?word=" + URLEncoder.encode(search, "UTF-8"));
        }
    }

    @OnGroup
    @Filters(value = {
            @Filter(value = "点歌", matchType = MatchType.STARTS_WITH)
    }, customFilter = "boot")
    public void music(GroupMsg msg, MsgSender sender) {
        System.out.println("点歌");
        String message = msg.getMsg();
        String music = message.trim().substring(3);
        SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), CQ.getMusic(music).toString());
    }

    @OnGroup
    @Filters(customFilter = "boot", value = {
            @Filter(value = "呼叫龙王")
    })
    public synchronized void findDragon(GroupMsg msg, MsgSender sender) {
        Map<String, Object> map = GlobalVariable.GROUP_DRAGON.get(msg.getGroupInfo().getGroupCode());
        if (map != null) {
            if (((Calendar) map.get("time")).getTimeInMillis() - System.currentTimeMillis() > 0) {
                SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), map.get("qq") != null ? CQ.at(map.get("qq") + "") : "当前暂无龙王");
                return;
            }
        }
        GroupUtils.getDragon(sender, msg.getGroupInfo().getGroupCode(), 0);
    }

    @OnGroup
    @Filters(customFilter = {"boot", "menu"}, customMostMatchType = MostMatchType.ALL)
    public void sendMenu(GroupMsg msg, MsgSender sender) {
        SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), "http://wuyourj.club/menu.html");
    }

    @OnGroup
    @Filters(customFilter = {"boot", "setu"}, customMostMatchType = MostMatchType.ALL)
    public void sendSetu(GroupMsg msg, MsgSender sender) {
        boolean r18 = msg.getMsg().toLowerCase().contains("r18");
        JSONObject json = getJson(r18 ? "1" : "0");
        JSONObject data = JSON.parseObject(json.getJSONArray("data").getString(0));
        System.out.println(data);
        String url = data.getString("url");
        String title = data.getString("title");
        String stringBuilder = "标题: " + title + "\n链接: " + url +
                "\n\napk链接: http://ii096.cn/IvS6lo";
        SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), stringBuilder);
//        SenderUtil.sendGroupMsg(msg.getGroupInfo().getGroupCode(), "涩图功能关闭啦,可以去http://wuyourj.club/apk/lolicon.apk下载app使用哦~");

    }

    @OnGroup
    @Filters(customFilter = {"boot", "setuImage"}, customMostMatchType = MostMatchType.ALL)
    public void sendSetuImage(GroupMsg msg, MsgSender sender) {
        sendSetu(msg, sender);
    }

    @OnGroup
    @Filters(customFilter = {"boot", "autistic1"}, customMostMatchType = MostMatchType.ALL)
    public void autistic1(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String fromQQ = msg.getAccountInfo().getAccountCode();
        if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
            sender.SETTER.setGroupBan(fromGroup, fromQQ, 1800);
            SenderUtil.sendGroupMsg(fromGroup, CQ.at(fromQQ) + "自闭成功" + CQ.getFace("178"));
        } else {
            SenderUtil.sendGroupMsg(fromGroup,
                    CQ.at(fromQQ) + "自闭失败,我没有禁言你的权限" + CQ.getFace("174"));
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "autistic2"}, customMostMatchType = MostMatchType.ALL)
    public void autistic2(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String fromQQ = msg.getAccountInfo().getAccountCode();
        String message = msg.getMsg();
        String sub = message.substring(message.indexOf("领取套餐") + 4);
        String end = sub.trim();
        if ("".equals(end)) {
            Random r = new Random();
            int time = r.nextInt(30) + 1;
            if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
                sender.SETTER.setGroupBan(fromGroup, fromQQ, time * 60);
                SenderUtil.sendGroupMsg(fromGroup,
                        CQ.at(fromQQ) + "恭喜领取了" + time + "分钟套餐" + CQ.getFace("178"));
            } else {
                SenderUtil.sendGroupMsg(fromGroup,
                        CQ.at(fromQQ) + "领取失败,我没有禁言你的权限" + CQ.getFace("201"));
            }
            return;
        }
        try {
            if (PowerUtils.powerCompare(msg, fromQQ, sender)) {
                String timeStr = sub.trim();
                int times = Integer.parseInt(timeStr);
                if (times > 1440 * 30) {
                    SenderUtil.sendGroupMsg(fromGroup, CQ.at(fromQQ) + "领取失败,时间不能超过30天!");
                    return;
                }
                int time = times * 60;
                timeStr = getTime(time);
                sender.SETTER.setGroupBan(fromGroup, fromQQ, times * 60L);
                SenderUtil.sendGroupMsg(fromGroup,
                        CQ.at(fromQQ) + "恭喜领取了"
                                + timeStr.replace("天0小时", "天").replace("小时0分钟", "小时").replace("分钟0秒", "分钟") + "的套餐"
                                + CQ.getFace("201"));
            } else {
                SenderUtil.sendGroupMsg(fromGroup,
                        CQ.at(fromQQ) + "领取失败,我没有禁言你的权限" + CQ.getFace("174"));
            }
        } catch (Exception e) {
            SenderUtil.sendGroupMsg(fromGroup, CQ.at(fromQQ) + "领取失败,指令不合法");
        }
    }

    private JSONObject getJson(String r18) {
        JSONObject json = null;
        try {
            String key1 = "820458705ebe071883b3c2";
            String key2 = "198111555ec3242d2c6b42";
            Map<String, String> params = new HashMap<>(8);
            params.put("apikey", key1);
            params.put("size1200", "true");
            params.put("r18", r18);
            String web = HttpUtils.get("http://api.lolicon.app/setu/", params, null).getResponse();
            json = JSONObject.parseObject(web);
            if (json.getInteger("code") == 429) {
                params.put("apikey", key2);
                web = HttpUtils.get("http://api.lolicon.app/setu/", params, null).getResponse();
                json = JSONObject.parseObject(web);
            }
//            QQAndroidBot
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("获取到json数据: " + json);
        return json;
    }
}
