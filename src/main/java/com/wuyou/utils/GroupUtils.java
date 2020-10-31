package com.wuyou.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.beans.messages.types.PowerType;
import com.forte.qqrobot.beans.messages.types.SexType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.JSONUtils;
import com.wuyou.entity.GroupEntity;
import com.wuyou.entity.GroupMemberEntity;

import java.util.*;

/**
 * @author wuyou
 */
public class GroupUtils {
    /**
     * 获取群列表
     *
     * @param sender MsgSender对象
     * @return 群实体类列表
     */
    public static List<GroupEntity> getGroupList(MsgSender sender) {
        try {
            String url = "https://qun.qq.com/cgi-bin/qun_mgr/get_group_list";
            Map<String, String> params = new HashMap<>(2);
            params.put("bkn", CookiesUtils.getBkn(sender));
            String request = HttpUtils.post(url, params, CookiesUtils.getCookies(sender)).getResponse();
            JSONObject jsonObject = JSONObject.parseObject(request);
            List<GroupEntity> list = new ArrayList<>();
            JSONArray create = jsonObject.getJSONArray("create");
            setGroupList(sender, create, list, PowerType.OWNER);
            JSONArray manage = jsonObject.getJSONArray("manage");
            setGroupList(sender, manage, list, PowerType.ADMIN);
            JSONArray join = jsonObject.getJSONArray("join");
            setGroupList(sender, join, list, PowerType.MEMBER);
            return list;
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "出现异常");
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 获取群成员信息
     *
     * @param sender MsgSender对象
     * @param group  群号
     * @param key    搜索关键词(可以为空, 为空返回所有群成员)
     * @return 群成员实体类列表
     */
    public static List<GroupMemberEntity> getGroupMembers(MsgSender sender, String group, String key) {
        String url = "https://qun.qq.com/cgi-bin/qun_mgr/search_group_members";
        Map<String, String> params = new HashMap<>(8);
        params.put("bkn", CookiesUtils.getBkn(sender));
        params.put("st", "0");
        params.put("end", "40");
        params.put("gc", group);
        if (key != null) {
            params.put("key", key);
        }
        String request = HttpUtils.post(url, params, CookiesUtils.getCookies(sender)).getResponse();
        System.out.println(request);
        try {
            JSONObject jsonObject = JSONObject.parseObject(request);
            List<GroupMemberEntity> list = new ArrayList<>();
            JSONArray mems = jsonObject.getJSONArray("mems");
            mems.forEach(mem -> {
                JSONObject object = (JSONObject) mem;
                GroupMemberEntity entity = JSONObject.toJavaObject(object, GroupMemberEntity.class);
                int role = object.getInteger("role");
                entity.setRole(PowerType.of(role == 0 ? 1 : role == 1 ? 0 : -1));
                int g = object.getInteger("g");
                entity.setSex(SexType.of(g == 0 ? -1 : g == -1 ? 0 : g));
                entity.setPoint(object.getJSONObject("lv").getInteger("point"));
                entity.setLevel(object.getJSONObject("lv").getInteger("level"));
                list.add(entity);
            });
            return list;
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "出现异常");
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<GroupMemberEntity> getGroupMembers(MsgSender sender, String group) {
        return getGroupMembers(sender, group, null);
    }

    public static void getDragon(MsgSender sender, String group) {
        System.out.println("获取" + group + "的龙王");
        try {
            Map<String, String> cookies = CookiesUtils.getCookies(sender);
            String url = "http://qun.qq.com/interactive/honorlist?gc=" + group + "&type=1&_wv=3&_wwv=129";
            String body = HttpUtils.get(url, null, cookies).getResponse();
            String jsonStr = body.substring(body.indexOf("__INITIAL_STATE__=") + 18);
            jsonStr = jsonStr.substring(0, jsonStr.indexOf("</script>"));
            JSONObject json = JSONUtils.toJsonObject(jsonStr);
            JSONObject currentTalkative = json.getJSONObject("currentTalkative");
            Map<String, Object> map = new HashMap<>(4);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 1);
            map.put("time", cal);
            if (currentTalkative != null) {
                String qq = currentTalkative.getString("uin");
                map.put("qq", qq);
                GlobalVariable.GROUP_DRAGON.put(group, map);
                if (qq != null) {
                    SenderUtil.sendGroupMsg(sender, group, CQ.at(qq));
                    return;
                }
            }
            GlobalVariable.GROUP_DRAGON.put(group, map);
            SenderUtil.sendGroupMsg(sender, group, "当前暂无龙王");
        } catch (Exception e) {
            SenderUtil.sendGroupMsg(sender, group, "获取失败");
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "出现异常");
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), e.getMessage());
        }
    }

    private static void setGroupList(MsgSender sender, JSONArray array, List<GroupEntity> list, PowerType powerType) {
        array.forEach(group -> {
            JSONObject object = (JSONObject) group;
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setRole(powerType);
            groupEntity.setUin(object.getLong("gc"));
            groupEntity.setGroupName(object.getString("gn"));
            groupEntity.setOwner(getGroupMembers(sender, object.getString("gc"), object.getString("owner")).get(0));
            list.add(groupEntity);
        });
    }

}
