package com.wuyou.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取别人的QQ等级
 *
 * @author wuyou
 */
public class LevelUtils {
    private static int num = 1;

    public static Integer getLevel(String qq) {
        System.out.println("开始获取 " + qq + "的QQ等级");
        String psKey = GlobalVariable.WEB_COOKIE.get("p_skey");
        String uin = GlobalVariable.WEB_COOKIE.get("uin");
        String url = "https://club.vip.qq.com/api/vip/getQQLevelInfo?requestBody=%7B%22sClientIp%22:%22127.0.0.1%22,%22sSessionKey%22:%22MQNYKeEUyV%22,%22iKeyType%22:1,%22iAppId%22:0,%22iUin%22:" + qq + "%7D";
        Map<String, String> cookie = new HashMap<>(2);
        cookie.put("uin", uin);
        cookie.put("p_skey", psKey);
        System.out.println("Cookie: " + cookie);
        String jsonString = HttpUtils.get(url, null, cookie).getResponse();
        JSONObject json;
        try {
            json = JSONObject.parseObject(jsonString);
        } catch (JSONException e) {
            if (++num > 2) {
                num = 1;
                System.out.println("使用新Cookie获取等级失败!");
                return -1;
            }
            System.out.println("Cookie失效, 重新登录获取");
            GlobalVariable.WEB_COOKIE.clear();
            GlobalVariable.WEB_COOKIE.putAll(WebCookiesUtils.getCookies());
            return getLevel(qq);
        }
        Integer level = json.getJSONObject("data").getJSONObject("mRes").getInteger("iQQLevel");
        System.out.println("获取成功! " + qq + "的等级为: " + level);
        return level;
    }
}
