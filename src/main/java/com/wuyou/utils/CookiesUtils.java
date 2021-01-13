package com.wuyou.utils;

import love.forte.simbot.api.message.results.AuthInfo;
import love.forte.simbot.api.sender.MsgSender;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取Cookie的util
 *
 * @author wuyou
 */
public class CookiesUtils {
    public static Map<String, String> getCookies(MsgSender sender) {
        try {

            AuthInfo.Cookies cookiesStr = sender.GETTER.getAuthInfo().getCookies();
            Map<String, String> cookies = new HashMap<>(8);
            cookies.putAll(cookiesStr.toMap());
            cookies.put("p_uin", cookies.get("uin"));
            return cookies;
        } catch (Exception e) {
            System.out.println("获取bkn报错: " + e.getMessage());
//            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static String getBkn(MsgSender sender) {
        try {
            return sender.GETTER.getAuthInfo().getToken();
        } catch (Exception e) {
            System.out.println("获取bkn报错: " + e.getMessage());
            return "";
        }
    }
}
