package com.wuyou.robot.listeners;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.wuyou.enums.FaceEnum;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.MessageService;
import com.wuyou.utils.*;
import love.forte.catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.results.GroupMemberList;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MostMatchType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator<br>
 * 2020年3月13日
 */
@Beans
public class GroupMessageListener {
    @Depend
    MessageService service;

    private static String getReqSign(Map<String, String> params) throws UnsupportedEncodingException {
        List<String> list = new ArrayList<>(params.keySet());
        list.sort((o1, o2) -> {
            char[] chars1 = o1.toCharArray();
            char[] chars2 = o2.toCharArray();
            int i = 0;
            while (i < chars1.length && i < chars2.length) {
                if (chars1[i] > chars2[i]) {
                    return 1;
                } else if (chars1[i] < chars2[i]) {
                    return -1;
                } else {
                    i++;
                }
            }
            return i == chars1.length ? -1 : i == chars2.length ? 1 : 0;
        });
        StringBuilder str = new StringBuilder();
        for (String string : list) {
            str.append(string).append("=").append(URLEncoder.encode(params.get(string), "UTF-8")).append("&");
        }
        str.append("app_key=").append("AEYJcmaShG0BokTZ");
        return SecureUtil.md5(str.toString()).toUpperCase();
    }

    @OnGroup
    @Filters(value = {
            @Filter("消息列表"),
            @Filter("查询问答"),
            @Filter("查询消息")
    }, customFilter = "boot")
    public void sendMessages(GroupMsg msg, MsgSender sender) {
        System.out.println("发送消息列表");
        String fromGroup = msg.getGroupInfo().getGroupCode();
        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.size() == 0) {
            SenderUtil.sendGroupMsg(fromGroup, "暂无回复记录");
            return;
        }
        GroupMemberList groupMemberList = sender.GETTER.getGroupMemberList(fromGroup);
        Set<String> qqSet = groupMemberList.stream().map(item -> item.getAccountInfo().getAccountCode()).collect(Collectors.toSet());
        map.forEach((key, str) -> {
            String newStr = getStr(sender, fromGroup, qqSet, str);
            map.put(key, newStr);
        });
        Map<String, String> newMap = new HashMap<>(map);
        map.forEach((key, str) -> {
            String newStr = getStr(sender, fromGroup, qqSet, key);
            newMap.remove(key);
            newMap.put(newStr, map.get(key));
        });
        StringBuilder mes = new StringBuilder(msg.getMsg() + ":\n");
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            mes.append("\t发送: \"").append(entry.getKey()).append("\"\t回复: \"").append(entry.getValue()).append("\"\n");
        }
        SenderUtil.sendGroupMsg(fromGroup, mes.toString().trim());
    }

    private String getStr(MsgSender sender, String fromGroup, Set<String> qqSet, String str) {
        Set<Neko> stringSet = CQ.getAtKqs(str);
        final String[] newStr = {str};
        stringSet.forEach(neko -> {
            if (qqSet.contains(neko.get("code"))) {
                newStr[0] = newStr[0].replace(neko, "@" + sender.GETTER.getMemberInfo(fromGroup, Objects.requireNonNull(neko.get("code"))).getAccountInfo().getAccountRemarkOrNickname());
            }
        });
        return newStr[0];
    }

    @OnGroup
    @Filters(customFilter = "boot")
    public void sendMessage(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String message = msg.getMsg();
        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.size() == 0) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (message.trim().equals(entry.getKey())) {
                SenderUtil.sendGroupMsg(fromGroup, entry.getValue());
                return;
            }
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "ai"}, customMostMatchType = MostMatchType.ALL)
    public void sendAiMessage(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String message = CQ.UTILS.removeByType(msg.getMsg(), "at", true, true);
        System.out.println(message);
        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.containsKey(msg.getMsg().trim())) {
            return;
        }
        List<Neko> faces = CQ.getKq(message, "face");
        for (Neko neko : faces) {
        System.out.println(neko.get("id"));
            String str = FaceEnum.getString(neko.get("id"));
            message = message.replace(neko, str);
        }
        if ("".equals(message)) {
            message = "在吗在吗";
        }
        if (!"783140627".equals(fromGroup)) {
            JSONObject json = RequestUtil.aiChat(message, fromGroup);
            if (json.getInteger("code") == 200) {
                String reply = json.getJSONArray("newslist").getJSONObject(0).getString("reply");
                if (!reply.isEmpty()) {
                    SenderUtil.sendGroupMsg(fromGroup, reply);
                    return;
                }
            }
            System.out.println("请求错误");
            sender.SENDER.sendPrivateMsg("1097810498", "聊天接口调用失败! 群号: " + fromGroup + ", 请求消息: " + message);
        } else {
///    @OnGroup
///    @Filters(customFilter = {"boot", "ai"}, customMostMatchType = MostMatchType.ALL)
///    public void sendAiMessage2(GroupMsg msg, MsgSender sender) {
            System.out.println(message);
            try {
                String signature;
                String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
                Map<String, String> params = params();
                params.put("session", fromGroup);
                params.put("question", message);
                signature = getReqSign(params);
                params.put("sign", signature);
                System.out.println("请求消息: " + message);
                // 获取网页数据
                String web = HttpUtils.get(url, params, null).getResponse();
                System.out.println("第1次请求");
                System.out.println("返回值: " + web);
                JSONObject json;
                json = JSONObject.parseObject(web);
                for (int i = 2; i < 11; i++) {
                    // 请求成功直接跳出
                    if (json.getInteger("ret") == 0) {
                        break;
                    }
                    // 请求失败重试
                    web = HttpUtils.get(url, params, null).getResponse();
                    json = JSONObject.parseObject(web);
                    System.out.println("第" + i + "次请求");
                    System.out.println("返回值: " + web);
                }
                JSONObject data = json.getJSONObject("data");
                if (json.getInteger("ret") == 0) {
                    SenderUtil.sendGroupMsg(fromGroup, data.getString("answer"));
                } else if (json.getInteger("ret") == 16394) {
                    SenderUtil.sendGroupMsg(fromGroup, "我没有听懂你的话");
                } else {
                    System.out.println(web);
                    System.out.println("请求错误");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "aiVoice"}, customMostMatchType = MostMatchType.ALL)
    public void sendAiVoice(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroupInfo().getGroupCode();
        String message = CQ.UTILS.remove(msg.getMsg(), true, true).substring(1);

        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.containsKey(msg.getMsg().trim())) {
            return;
        }
        if ("说".equals(message)) {
            message = "你想让我说什么";
        }
        List<Neko> faces = CQ.getKq(message, "face");
        for (Neko neko : faces) {
            String str = FaceEnum.getString(neko.get("id"));
            message = message.replace(neko, "".equals(str) ? neko : str);
        }
        try {
            String signature;
            String url = "https://api.ai.qq.com/fcgi-bin/aai/aai_tts";
            Map<String, String> params = params();
            params.put("speaker", "6");
            params.put("format", "2");
            params.put("volume", "8");
            params.put("speed", "95");
            params.put("text", message);
            params.put("aht", "10");
            params.put("apc", "40");
            signature = getReqSign(params);
            params.put("sign", signature);
            System.out.println("请求消息: " + message);
            // 获取网页数据
            JSONObject json = null;
            int num = 0;
            do {
                num++;
                String web;
                try {
                    web = HttpUtils.get(url, params, null).getResponse();
                } catch (Exception e) {
                    continue;
                }
                json = JSONObject.parseObject(web);
                if (num > 5) {
                    break;
                }
            } while (Objects.requireNonNull(json).getInteger("ret") != 0);
            JSONObject data = json.getJSONObject("data");
            if (json.getInteger("ret") == 0) {
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] d = decoder.decode(data.getString("speech"));
                for (int i = 0; i < d.length; i++) {
                    if (d[i] < 0) {
                        d[i] += 256;
                    }
                }
                File path = new File(CQ.getCQPath() + "/data/record/");
                if (!path.exists()) {
                    System.out.println(path.mkdirs());
                }
                String filePath = data.getString("md5sum");

                FileOutputStream fos = new FileOutputStream(path + "/" + filePath);
                System.out.println(path + "/" + filePath);
                fos.write(d);
                fos.flush();
                fos.close();
                SenderUtil.sendGroupMsg(fromGroup, CQ.getRecord(path + "/" + filePath).toString());
            } else {
                SenderUtil.sendGroupMsg(fromGroup, CQ.at(msg.getAccountInfo().getAccountCode()) + "小忧没有看懂你说的是什么~");
                System.out.println("请求错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> params() {
        Map<String, String> params = new HashMap<>(8);
        params.put("app_id", "2127860232");
        String time = Long.toString(System.currentTimeMillis() / 1000);
        String uuid = UUID.randomUUID().toString();
        String nonceStr = uuid.replaceAll("-", "");
        params.put("time_stamp", time);
        params.put("nonce_str", nonceStr);
        return params;
    }

    @OnGroup
    @Filters(customFilter = {"boot", "addMessage"}, customMostMatchType = MostMatchType.ALL)
    public void addMessage(GroupMsg msg, MsgSender sender) {
        String mess = msg.getMsg();
        String group = msg.getGroupInfo().getGroupCode();
        String qq = msg.getAccountInfo().getAccountCode();
        System.out.println(mess);
        if (mess.toLowerCase().contains("atall")) {
            SenderUtil.sendGroupMsg(group, CQ.at(qq) + "添加失败: 不允许有艾特全体");
            return;
        }
        if (PowerUtils.getPermissions(group, qq, sender) > 1) {
            System.out.println("执行添加消息代码");
            String message = mess.substring(mess.indexOf("添加消息") + 4, mess.indexOf("回复")).trim();
            String answer = mess.substring(mess.indexOf("回复") + 2).trim();
            if ("".equals(message) || "".equals(answer)) {
                SenderUtil.sendGroupMsg(group, CQ.at(qq) + "添加失败: 内容不完整");
                return;
            }
            try {
                if (service.addMessage(group, message, answer) == 1) {
                    SenderUtil.sendGroupMsg(group,
                            CQ.at(qq) + "\n添加成功: \n\t\t消息内容: " + message + "\n\t\t回复内容: " + answer);
                } else {
                    SenderUtil.sendGroupMsg(group,
                            CQ.at(qq) + "\n更改成功: \n\t\t消息内容: " + message + "\n\t\t回复内容已替换为: " + answer);
                }
            } catch (ObjectExistedException e) {
                SenderUtil.sendGroupMsg(group, CQ.at(qq) + "\n更改失败: \n此条消息已存在");
            }
        } else {
            SenderUtil.sendGroupMsg(group, "添加失败,你不是我的管理员!");
        }
    }

    @OnGroup
    @Filters(customFilter = {"boot", "removeMessage"}, customMostMatchType = MostMatchType.ALL)
    public void removeMessage(GroupMsg msg, MsgSender sender) {
        String mess = msg.getMsg();
        String group = msg.getGroupInfo().getGroupCode();
        String qq = msg.getAccountInfo().getAccountCode();
        if (PowerUtils.getPermissions(group, qq, sender) > 1) {
            System.out.println("执行删除消息代码");
            String message = mess.substring(mess.indexOf("删除消息") + 4).trim();
            if ("".equals(message)) {
                SenderUtil.sendGroupMsg(group, CQ.at(qq) + "删除失败: 内容不完整");
                return;
            }
            try {
                service.removeMessage(group, message);
                SenderUtil.sendGroupMsg(group, CQ.at(qq) + "\n删除成功: \n\t\t消息内容: " + message);
            } catch (ObjectNotFoundException e) {
                SenderUtil.sendGroupMsg(group, CQ.at(qq) + "\n更改失败: \n没找到此条消息");
            }
        } else {
            SenderUtil.sendGroupMsg(group, "删除失败,你不是我的管理员!");
        }
    }

}
