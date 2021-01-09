package com.wuyou.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyou
 */
public class RequestUtil {
    public static JSONObject aiChat(String message, String user){
        String url = "http://api.tianapi.com/txapi/tuling/index";
        Map<String, String> params = new HashMap<>(4);
        params.put("key", "9845b4e0442683f1f8ab813c35180fc5");
        params.put("question", message);
        params.put("user", user);
        String web = HttpUtils.get(url, params, null).getResponse();
        System.out.println("请求: " + message);
        System.out.println("返回值: " + web);
        return JSONObject.parseObject(web);
    }
}
