package com.wuyou.utils;

import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.exception.ConfigurationException;
import com.wuyou.entity.RequestEntity;
import com.wuyou.exception.JavaScriptNotFoundException;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟网页登录vip.qq.com, 获取Cookie
 *
 * @author wuyou
 */
@Beans
@Component
public class WebCookiesUtils {

    private static String botInfo;
    private static String uin;
    private static String pwd;

    @Value("${core.bots}")
    public void setBotInfo(final String botInfo) {
        setBotInfoStatic(botInfo);
    }

    public static void setBotInfoStatic(final String botInfo) {
        WebCookiesUtils.botInfo = botInfo;
    }

    public static void registerBotsFormatter(String registerBots) {
        if (registerBots == null || (registerBots = registerBots.trim()).length() == 0) {
            return;
        }
        // 根据逗号切割
        String botInfo = registerBots;
        if (botInfo.trim().length() == 0) {
            throw new ConfigurationException("configuration 'core.bots' is malformed.");
        }
        int first = botInfo.indexOf(":");
        String code = botInfo.substring(0, first).trim();
        String path = botInfo.substring(first + 1).trim().replace("\\,", ",");
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        uin = code;
        pwd = path;
    }

    public static Map<String, String> getCookies() {
        Map<String, String> cookies = new HashMap<>(32);
        try {

            registerBotsFormatter(botInfo);
            //模拟账号密码登录vip.qq.com获取验证信息
            String url = "https://ssl.ptlogin2.qq.com/check?pt_tea=2&pt_vcode=1&uin=" + uin + "&appid=8000201&js_ver=20092719&js_type=1&u1=https%3A%2F%2Fvip.qq.com%2Floginsuccess.html&r=0.7082049514319555&pt_uistyle=40";
            String[] web = getArr(url, 13);
            if (Integer.parseInt(web[0]) == 0) {
                Map<String, String> map = new HashMap<>(16);
                map.put("qq", uin);
                map.put("pwd", pwd);
                map.put("ret", web[0]);
                map.put("code", web[1]);
                map.put("salt", web[2]);
                map.put("verifysession", web[3]);
                map.put("isRandSalt", web[4]);
                map.put("ptdrvs", web[5]);
                map.put("sessionID", web[6]);
                // 执行js代码加密密码并获取登录链接
                long start = System.currentTimeMillis();
                InputStream in = LevelUtils.class.getClassLoader().getResourceAsStream("js/getPSkey.js");
                if (in == null) {
                    System.out.println("js文件获取失败!");
                    throw new JavaScriptNotFoundException();
                }
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
                engine.eval(new BufferedReader(new InputStreamReader(in)));
                Invocable inv = (Invocable) engine;
                String loginUrl = inv.invokeFunction("getUrl", map.get("qq"), map.get("pwd"), map.get("code"), map.get("verifysession"), map.get("ptdrvs"), map.get("sessionID")).toString();
                System.out.println("JS执行耗时: " + (System.currentTimeMillis() - start));
                String[] result = getArr(loginUrl, 7);
                // 设置Cookie
                if (Integer.parseInt(result[0]) == 0) {
                    RequestEntity requestEntity = HttpUtils.get(result[2]);
                    List<Cookie> cookieList = requestEntity.getCookies();
                    cookieList.forEach(cookie -> cookies.put(cookie.getName(), cookie.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("网页登录获取到的cookie: " + cookies);
        return cookies;
    }

    private static String[] getArr(String url, int subIndex) {
        String result = HttpUtils.get(url).getResponse().replace("'", "");
        return result.substring(subIndex, result.length() - 1).split(",");
    }
}
