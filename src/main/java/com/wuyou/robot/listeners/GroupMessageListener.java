package com.wuyou.robot.listeners;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.CQCodeTypes;
import com.forte.qqrobot.beans.types.MostDIYType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.forte.qqrobot.utils.JSONUtils;
import com.forte.utils.basis.MD5Utils;
import com.wuyou.enums.FaceEnum;
import com.wuyou.exception.ObjectExistedException;
import com.wuyou.exception.ObjectNotFoundException;
import com.wuyou.service.MessageService;
import com.wuyou.utils.CQ;
import com.wuyou.utils.PowerUtils;

/**
 * @author Administrator<br>
 *         2020年3月13日
 *
 */
@Beans
public class GroupMessageListener {
	@Depend
	MessageService service;
	List<String> administrator = new ArrayList<String>();

	public GroupMessageListener() {
		administrator.add("1097810498");
		administrator.add("1041025733");
		administrator.add("2973617637");
	}

	@Listen(MsgGetTypes.groupMsg)
	@Filter(value = { "消息列表", "查询问答", "查询消息" }, diyFilter = "boot")
	public void sendMessages(GroupMsg msg, MsgSender sender) {
		System.out.println("发送消息列表");
		String fromGroup = msg.getGroup();
		Map<String, String> map = service.getAllByGroup(fromGroup);
		if (map.size() == 0) {
			sender.SENDER.sendGroupMsg(fromGroup, "暂无回复记录");
			return;
		}
		StringBuilder mes = new StringBuilder(msg.getMsg() + ":\n");
		for (String str : map.keySet()) {
			mes.append("\t发送: \"" + str + "\"\t回复: \"" + map.get(str) + "\"\n");
		}
		sender.SENDER.sendGroupMsg(fromGroup, mes.toString().trim());
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
		for (String str : map.keySet()) {
			if (message.trim().equals(str)) {
				sender.SENDER.sendGroupMsg(fromGroup, map.get(str));
				return;
			}
		}
	}

	@Listen(MsgGetTypes.groupMsg)
	@Filter(diyFilter = { "boot", "ai" }, mostDIYType = MostDIYType.EVERY_MATCH)
	public void sendAiMessage(GroupMsg msg, MsgSender sender) {
		String fromGroup = msg.getGroup();
		String atThis = CQ.at(msg.getThisCode());
		String message = msg.getMsg().replace(atThis.trim(), "").trim();

		Map<String, String> map = service.getAllByGroup(fromGroup);
		if (map.keySet().contains(msg.getMsg().trim())) {
			return;
		}
		if ("".equals(message))
			message = "在吗在吗";
		List<CQCode> fases = CQCodeUtil.build().getCQCodeFromMsgByType(message, CQCodeTypes.face);
		for (CQCode CQCode : fases) {
			String str = FaceEnum.getString(CQCode.getParam("id"));
			message = message.replace(CQCode, "".equals(str) ? CQCode : str);
		}
		try {
			String signature = "";
			String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
			Map<String, String> params = new HashMap<String, String>();
			params.put("app_id", "2127860232");
			String time = Long.toString(System.currentTimeMillis() / 1000);
			String uuid = UUID.randomUUID().toString();
			String nonce_str = uuid.replaceAll("-", "");
			String session = fromGroup;
			String question = message;
			params.put("time_stamp", time);
			params.put("nonce_str", nonce_str);
			params.put("session", session);
			params.put("question", question);
			signature = getReqSign(params, "AEYJcmaShG0BokTZ");
			params.put("sign", signature);
			System.out.println("请求消息: " + question);
			// 获取网页数据
			String web = Jsoup.connect(url).data(params).ignoreContentType(true).get().text();
			System.out.println("第1次请求");
			System.out.println("返回值: " + web);
			JSONObject json = new JSONObject();
			json = JSONUtils.toJsonObject(web);
			for (int i = 2; i < 11; i++) {
				// 请求成功直接跳出
				if (json.getInteger("ret") == 0)
					break;
				// 请求失败重试
				web = Jsoup.connect(url).data(params).ignoreContentType(true).get().text();
				json = JSONUtils.toJsonObject(web);
				System.out.println("第" + i + "次请求");
				System.out.println("返回值: " + web);
			}
			JSONObject data = json.getJSONObject("data");
			if (json.getInteger("ret") == 0)
				sender.SENDER.sendGroupMsg(fromGroup, data.getString("answer"));
			else if (json.getInteger("ret") == 16394)
				sender.SENDER.sendGroupMsg(fromGroup, "我没有听懂你的话");
			else {
				System.out.println(web);
				System.out.println("请求错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Listen(MsgGetTypes.groupMsg)
	@Filter(diyFilter = { "boot", "aiVoice" }, mostDIYType = MostDIYType.EVERY_MATCH)
	public void sendAiVoice(GroupMsg msg, MsgSender sender) {
		String fromGroup = msg.getGroup();
		String atThis = CQ.at(msg.getThisCode());
		String message = msg.getMsg().replace(atThis.trim(), "").trim().substring(1);

		Map<String, String> map = service.getAllByGroup(fromGroup);
		if (map.keySet().contains(msg.getMsg().trim())) {
			return;
		}
		if ("说".equals(message))
			message = "你想让我说什么";
		List<CQCode> fases = CQCodeUtil.build().getCQCodeFromMsgByType(message, CQCodeTypes.face);
		for (CQCode CQCode : fases) {
			String str = FaceEnum.getString(CQCode.getParam("id"));
			message = message.replace(CQCode, "".equals(str) ? CQCode : str);
		}
		try {
			String signature = "";
			String url = "https://api.ai.qq.com/fcgi-bin/aai/aai_tts";
			Map<String, String> params = new HashMap<String, String>();
			params.put("app_id", "2127860232");
			String time = Long.toString(System.currentTimeMillis() / 1000);
			String uuid = UUID.randomUUID().toString();
			String nonce_str = uuid.replaceAll("-", "");
			String question = message;
			params.put("time_stamp", time);
			params.put("nonce_str", nonce_str);
			params.put("speaker", "6");
			params.put("format", "2");
			params.put("volume", "5");
			params.put("speed", "95");
			params.put("text", question);
			params.put("aht", "10");
			params.put("apc", "40");
			signature = getReqSign(params, "AEYJcmaShG0BokTZ");
			params.put("sign", signature);
			System.out.println("请求消息: " + question);
			// 获取网页数据
			JSONObject json = null;
			int num = 0;
			do {
				num++;
				String web = null;
				try {
					web = Jsoup.connect(url).data(params).ignoreContentType(true).get().text();
//					System.out.println(web);
				} catch (Exception e) {
					continue;
				}
				json = JSONUtils.toJsonObject(web);
				if (num > 5) {
					break;
				}
			} while (json.getInteger("ret") != 0);
			JSONObject data = json.getJSONObject("data");
			if (json.getInteger("ret") == 0) {
				Decoder decoder = Base64.getDecoder();
				byte[] d = decoder.decode(data.getString("speech"));
				for (int i = 0; i < d.length; i++) {
					if (d[i] < 0) {
						d[i] += 256;
					}
				}
				String path = CQ.getCQPath() + "/data/record/";
				String filePath = data.getString("md5sum");
				FileOutputStream fos = new FileOutputStream(path + filePath);
				System.out.println(path + filePath);
				fos.write(d);
				fos.flush();
				fos.close();
				sender.SENDER.sendGroupMsg(fromGroup, CQCodeUtil.build().getCQCode_Record(filePath).toString());
			} else {
				sender.SENDER.sendGroupMsg(fromGroup, CQ.at(msg.getQQ()) + "小忧没有看懂你说的是什么~");
				System.out.println("请求错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Listen(MsgGetTypes.groupMsg)
	@Filter(diyFilter = { "boot", "addMessage" }, mostDIYType = MostDIYType.EVERY_MATCH)
	public void addMessage(GroupMsg msg, MsgSender sender) {
		String mess = msg.getMsg();
		String group = msg.getGroup();
		String qq = msg.getQQ();
		if (PowerUtils.getPowerType(group, qq, sender) > 1) {
			System.out.println("执行添加消息代码");
			String message = mess.substring(mess.indexOf("添加消息") + 4, mess.indexOf("回复")).trim();
			String answer = mess.substring(mess.indexOf("回复") + 2).trim();
			if (message.equals("") || answer.equals("")) {
				sender.SENDER.sendGroupMsg(group, CQ.at(qq) + "添加失败: 内容不完整");
				return;
			}
			try {
				if (service.addMessage(group, message, answer) == 1) {
					sender.SENDER.sendGroupMsg(group,
							CQ.at(qq) + "\n添加成功: \n\t\t消息内容: " + message + "\n\t\t回复内容: " + answer);
				} else {
					sender.SENDER.sendGroupMsg(group,
							CQ.at(qq) + "\n更改成功: \n\t\t消息内容: " + message + "\n\t\t回复内容已替换为: " + answer);
				}
			} catch (ObjectExistedException e) {
				sender.SENDER.sendGroupMsg(group, CQ.at(qq) + "\n更改失败: \n此条消息已存在");
				return;
			}
		} else {
			sender.SENDER.sendGroupMsg(group, "添加失败,你不是我的管理员!");
		}
	}

	@Listen(MsgGetTypes.groupMsg)
	@Filter(diyFilter = { "boot", "removeMessage" }, mostDIYType = MostDIYType.EVERY_MATCH)
	public void removeMessage(GroupMsg msg, MsgSender sender) {
		String mess = msg.getMsg();
		String group = msg.getGroup();
		String qq = msg.getQQ();
		if (PowerUtils.getPowerType(group, qq, sender) > 1) {
			System.out.println("执行删除消息代码");
			String message = mess.substring(mess.indexOf("删除消息") + 4).trim();
			if (message.equals("")) {
				sender.SENDER.sendGroupMsg(group, CQ.at(qq) + "删除失败: 内容不完整");
				return;
			}
			try {
				service.removeMessage(group, message);
				sender.SENDER.sendGroupMsg(group, CQ.at(qq) + "\n删除成功: \n\t\t消息内容: " + message);
			} catch (ObjectNotFoundException e) {
				sender.SENDER.sendGroupMsg(group, CQ.at(qq) + "\n更改失败: \n没找到此条消息");
				return;
			}
		} else {
			sender.SENDER.sendGroupMsg(group, "删除失败,你不是我的管理员!");
		}
	}

	private static String getReqSign(Map<String, String> params, String appkey) throws UnsupportedEncodingException {
		List<String> list = new ArrayList<String>();
		for (String key : params.keySet()) {
			list.add(key);
		}
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				char[] chars1 = o1.toCharArray();
				char[] chars2 = o2.toCharArray();
				int i = 0;
				while (i < chars1.length && i < chars2.length) {
					if (chars1[i] > chars2[i])
						return 1;
					else if (chars1[i] < chars2[i])
						return -1;
					else
						i++;
				}
				return i == chars1.length ? -1 : i == chars2.length ? 1 : 0;
			}
		});
		StringBuffer str = new StringBuffer();
		for (String string : list) {
			str.append(string + "=" + URLEncoder.encode(params.get(string), "utf-8") + "&");
		}
		str.append("app_key=" + appkey);
		return MD5Utils.toMD5(str.toString()).toUpperCase();
	}
}