package com.wuyou.robot.listeners;

import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberList;
import com.forte.qqrobot.beans.messages.result.inner.GroupMember;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.MostDIYType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.JSONUtils;
import com.forte.utils.basis.MD5Utils;
import com.simplerobot.modules.utils.KQCode;
import com.wuyou.enums.FaceEnum;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.MessageService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.HttpUtils;
import com.wuyou.utils.PowerUtils;
import com.wuyou.utils.SenderUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Base64.Decoder;
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
        return MD5Utils.toMD5(str.toString()).toUpperCase();
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = {"消息列表", "查询问答", "查询消息"}, diyFilter = "boot")
    public void sendMessages(GroupMsg msg, MsgSender sender) {
        System.out.println("发送消息列表");
        String fromGroup = msg.getGroup();
        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.size() == 0) {
            SenderUtil.sendGroupMsg(sender, fromGroup, "暂无回复记录");
            return;
        }
        GroupMemberList groupMemberList = sender.GETTER.getGroupMemberList(fromGroup);
        Set<String> qqSet = groupMemberList.stream().map(GroupMember::getQQ).collect(Collectors.toSet());
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
        SenderUtil.sendGroupMsg(sender, fromGroup, mes.toString().trim());
    }

    private String getStr(MsgSender sender, String fromGroup, Set<String> qqSet, String str) {
        Set<KQCode> stringSet = CQ.getAtKqs(str);
        final String[] newStr = {str};
        stringSet.forEach(kqCode -> {
            if (qqSet.contains(kqCode.get("qq"))) {
                newStr[0] = newStr[0].replace(kqCode, "@" + sender.GETTER.getGroupMemberInfo(fromGroup, kqCode.get("qq")).getRemarkOrNickname());
            }
        });
        return newStr[0];
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = "boot")
    public void sendMessage(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String message = msg.getMsg();
        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.size() == 0) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (message.trim().equals(entry.getKey())) {
                SenderUtil.sendGroupMsg(sender, fromGroup, entry.getValue());
                return;
            }
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "ai"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void sendAiMessage(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String message = CQ.UTILS.removeByType("at", msg.getMsg(), true, true);

        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.containsKey(msg.getMsg().trim())) {
            return;
        }
        List<KQCode> fases = CQ.getKq(message, "face");
        for (KQCode KQCode : fases) {
            String str = FaceEnum.getString(KQCode.get("id"));
            message = message.replace(KQCode, str);
        }
        if ("".equals(message)) {
            message = "在吗在吗";
        }

        String url = "http://api.tianapi.com/txapi/tuling/index";
        Map<String, String> params = params();
        params.put("key", "9845b4e0442683f1f8ab813c35180fc5");
        params.put("question", message);
        params.put("user", fromGroup);
        String web = HttpUtils.get(url, params, null).getResponse();
        System.out.println("请求: " + message);
        System.out.println("返回值: " + web);
        JSONObject json = JSONUtils.toJsonObject(web);
        if (json.getInteger("code") == 200) {
            String reply = json.getJSONArray("newslist").getJSONObject(0).getString("reply");
            if (!reply.isEmpty()) {
                SenderUtil.sendGroupMsg(sender, fromGroup, reply);
                return;
            }
        }
        System.out.println("请求错误");
        sender.SENDER.sendPrivateMsg("1097810498", "聊天接口调用失败! 群号: " + fromGroup + ", 请求消息: " + message);
    }
///    @Listen(MsgGetTypes.groupMsg)
///    @Filter(diyFilter = {"boot", "ai"}, mostDIYType = MostDIYType.EVERY_MATCH)
///    public void sendAiMessage(GroupMsg msg, MsgSender sender) {
///        String fromGroup = msg.getGroup();
///        String message = CQ.utils.removeByType("at", msg.getMsg(), true, true);
///
///        Map<String, String> map = service.getAllByGroup(fromGroup);
///        if (map.containsKey(msg.getMsg().trim())) {
///            return;
///        }
///        List<KQCode> fases = CQ.getKq(message, "face");
///        for (KQCode KQCode : fases) {
///            String str = FaceEnum.getString(KQCode.get("id"));
///            message = message.replace(KQCode, str);
///        }
///        if ("".equals(message))
///            message = "在吗在吗";
///
///        System.out.println(message);
///        try {
///            String signature;
///            String url = "http://api.tianapi.com/txapi/tuling/index";
///            Map<String, String> params = params();
///            params.put("session", fromGroup);
///            params.put("question", message);
///            signature = getReqSign(params);
///            params.put("sign", signature);
///            System.out.println("请求消息: " + message);
///            // 获取网页数据
///            String web = HttpUtils.get(url, params, null).getResponse();
///            System.out.println("第1次请求");
///            System.out.println("返回值: " + web);
///            JSONObject json;
///            json = JSONUtils.toJsonObject(web);
///            for (int i = 2; i < 11; i++) {
///                // 请求成功直接跳出
///                if (json.getInteger("ret") == 0)
///                    break;
///                // 请求失败重试
///                web = HttpUtils.get(url, params, null).getResponse();
///                json = JSONUtils.toJsonObject(web);
///                System.out.println("第" + i + "次请求");
///                System.out.println("返回值: " + web);
///            }
///            JSONObject data = json.getJSONObject("data");
///            if (json.getInteger("ret") == 0)
///                SenderUtil.sendGroupMsg(sender, fromGroup, data.getString("answer"));
///            else if (json.getInteger("ret") == 16394)
///                SenderUtil.sendGroupMsg(sender, fromGroup, "我没有听懂你的话");
///            else {
///                System.out.println(web);
///                System.out.println("请求错误");
///            }
///        } catch (Exception e) {
///            e.printStackTrace();
///        }
///    }


    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "aiVoice"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void sendAiVoice(GroupMsg msg, MsgSender sender) {
        String fromGroup = msg.getGroup();
        String message = CQ.UTILS.remove(msg.getMsg(), true, true).substring(1);

        Map<String, String> map = service.getAllByGroup(fromGroup);
        if (map.containsKey(msg.getMsg().trim())) {
            return;
        }
        if ("说".equals(message)) {
            message = "你想让我说什么";
        }
        List<KQCode> faces = CQ.getKq(message, "face");
        for (KQCode KQCode : faces) {
            String str = FaceEnum.getString(KQCode.get("id"));
            message = message.replace(KQCode, "".equals(str) ? KQCode : str);
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
                json = JSONUtils.toJsonObject(web);
                if (num > 5) {
                    break;
                }
            } while (Objects.requireNonNull(json).getInteger("ret") != 0);
            JSONObject data = json.getJSONObject("data");
            if (json.getInteger("ret") == 0) {
                Decoder decoder = Base64.getDecoder();
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
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.getRecord(path + "/" + filePath).toString());
            } else {
                SenderUtil.sendGroupMsg(sender, fromGroup, CQ.at(msg.getQQ()) + "小忧没有看懂你说的是什么~");
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

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "addMessage"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void addMessage(GroupMsg msg, MsgSender sender) {
        String mess = msg.getMsg();
        String group = msg.getGroup();
        String qq = msg.getQQ();
        System.out.println(mess);
        if (mess.toLowerCase().contains("atall")) {
            SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "添加失败: 不允许有艾特全体");
            return;
        }
        if (PowerUtils.getPowerType(group, qq, sender) > 1) {
            System.out.println("执行添加消息代码");
            String message = mess.substring(mess.indexOf("添加消息") + 4, mess.indexOf("回复")).trim();
            String answer = mess.substring(mess.indexOf("回复") + 2).trim();
            if ("".equals(message) || "".equals(answer)) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "添加失败: 内容不完整");
                return;
            }
            try {
                if (service.addMessage(group, message, answer) == 1) {
                    SenderUtil.sendGroupMsg(sender, group,
                            CQ.at(qq) + "\n添加成功: \n\t\t消息内容: " + message + "\n\t\t回复内容: " + answer);
                } else {
                    SenderUtil.sendGroupMsg(sender, group,
                            CQ.at(qq) + "\n更改成功: \n\t\t消息内容: " + message + "\n\t\t回复内容已替换为: " + answer);
                }
            } catch (ObjectExistedException e) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n更改失败: \n此条消息已存在");
            }
        } else {
            SenderUtil.sendGroupMsg(sender, group, "添加失败,你不是我的管理员!");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(diyFilter = {"boot", "removeMessage"}, mostDIYType = MostDIYType.EVERY_MATCH)
    public void removeMessage(GroupMsg msg, MsgSender sender) {
        String mess = msg.getMsg();
        String group = msg.getGroup();
        String qq = msg.getQQ();
        if (PowerUtils.getPowerType(group, qq, sender) > 1) {
            System.out.println("执行删除消息代码");
            String message = mess.substring(mess.indexOf("删除消息") + 4).trim();
            if ("".equals(message)) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "删除失败: 内容不完整");
                return;
            }
            try {
                service.removeMessage(group, message);
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n删除成功: \n\t\t消息内容: " + message);
            } catch (ObjectNotFoundException e) {
                SenderUtil.sendGroupMsg(sender, group, CQ.at(qq) + "\n更改失败: \n没找到此条消息");
            }
        } else {
            SenderUtil.sendGroupMsg(sender, group, "删除失败,你不是我的管理员!");
        }
    }
}
