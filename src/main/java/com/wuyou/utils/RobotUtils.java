package com.wuyou.utils;

import com.forte.qqrobot.sender.MsgSender;

import java.util.HashMap;
import java.util.Map;

public class RobotUtils {
    public static Map<String, String> getCookies(MsgSender sender) {
        String cookiesStr = sender.GETTER.getAuthInfo().getCookies();
        Map<String, String> cookies = new HashMap<>();
        for (String cookie : cookiesStr.split(";")) {
            cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
        }
        cookies.put("p_uin", cookies.get("uin"));
        return cookies;
    }

    public static String getBkn(MsgSender sender) {
        return sender.GETTER.getAuthInfo().getCsrfToken();
    }
}
