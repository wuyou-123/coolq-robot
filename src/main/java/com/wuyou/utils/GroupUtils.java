package com.wuyou.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyou.entity.GroupEntity;
import com.wuyou.entity.GroupMemberEntity;
import com.wuyou.enums.SexType;
import love.forte.simbot.api.message.assists.Permissions;
import love.forte.simbot.api.sender.MsgSender;

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
            setGroupList(sender, create, list, Permissions.OWNER);
            JSONArray manage = jsonObject.getJSONArray("manage");
            setGroupList(sender, manage, list, Permissions.ADMINISTRATOR);
            JSONArray join = jsonObject.getJSONArray("join");
            setGroupList(sender, join, list, Permissions.MEMBER);
            return list;
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "出现异常");
            if ("null".equals(e.getMessage()) || e.getMessage() == null) {
                sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "空指针");
                return new ArrayList<>();
            }
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
                entity.setRole(role == 0 ? Permissions.OWNER : role == 1 ? Permissions.ADMINISTRATOR : Permissions.MEMBER);
                int g = object.getInteger("g");
                entity.setSex(g == 0 ? SexType.MALE : g == 1 ? SexType.FEMALE : SexType.OTHER);
                entity.setPoint(object.getJSONObject("lv").getInteger("point"));
                entity.setLevel(object.getJSONObject("lv").getInteger("level"));
                list.add(entity);
            });
            return list;
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "出现异常");
            System.out.println(e.getMessage());
            if ("null".equals(e.getMessage()) || e.getMessage() == null) {
                sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "空指针");
                return new ArrayList<>();
            }
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<GroupMemberEntity> getGroupMembers(MsgSender sender, String group) {
        return getGroupMembers(sender, group, null);
    }

    public static void getDragon(final MsgSender sender, final String group, int type) {
        System.out.println("获取" + group + "的龙王");
        try {
//            Map<String, String> cookies = CookiesUtils.getCookies(sender);
//            System.out.println("请求的cookie: " + cookies);
//            String url = "http://qun.qq.com/interactive/honorlist?gc=" + group + "&type=1&_wv=3&_wwv=129";
//            String url1 = "http://qun.qq.com/interactive/qunhonor?gc=" + group + "&_wv=3&&_wwv=128&dragon_gray1";
//
//            String body = HttpUtils.get(type == 0 ? url : url1, null, cookies).getResponse();
//            JSONObject json = getJson(body);
//            System.out.println(json);
//            JSONObject currentTalkative = json.getJSONObject("currentTalkative");
//            Map<String, Object> map = new HashMap<>(4);
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.MINUTE, 1);
//            map.put("time", cal);
//            if (currentTalkative != null) {
//                String qq = currentTalkative.getString("uin");
//                map.put("qq", qq);
//                GlobalVariable.GROUP_DRAGON.put(group, map);
//                if (qq != null) {
//                    SenderUtil.sendGroupMsg(group, CQ.at(qq));
//                    return;
//                } else {
//                    if (type == 0) {
//                        getDragon(sender, group, 1);
//                        return;
//                    }
//                }
//            }
//            GlobalVariable.GROUP_DRAGON.put(group, map);
            SenderUtil.sendGroupMsg(group, "功能暂时停用");
        } catch (Exception e) {
            SenderUtil.sendGroupMsg(group, "获取失败");
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "出现异常");
            if ("null".equals(e.getMessage()) || e.getMessage() == null) {
                sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "空指针");
                return;
            }
            sender.SENDER.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), e.getMessage());
        }
    }

    private static JSONObject getJson(String body) {
        String jsonStr = body.substring(body.indexOf("__INITIAL_STATE__=") + 18);
        jsonStr = jsonStr.substring(0, jsonStr.indexOf("</script>"));
        return JSONObject.parseObject(jsonStr);
    }

    private static void setGroupList(MsgSender sender, JSONArray array, List<GroupEntity> list, Permissions powerType) {
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
