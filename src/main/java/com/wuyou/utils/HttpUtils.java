package com.wuyou.utils;

import com.wuyou.entity.RequestEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.CookieStore;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求Util, 有的话可以用别的代替, 目前只有get方法
 *
 * @author wuyou
 */
public class HttpUtils {
    static CookieStore store;
    static CloseableHttpClient closeableHttpClient;

    static {
        store = new BasicCookieStore();
        closeableHttpClient = HttpClients.custom().setDefaultCookieStore(store).build();
    }

    public static RequestEntity get(String url, Map<String, String> params, Map<String, String> cookies) {
        RequestEntity requestEntity = new RequestEntity();
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                params.forEach(uriBuilder::addParameter);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            if (cookies != null) {
                StringBuilder cookie = new StringBuilder();
                cookies.forEach((key, value) -> cookie.append(key).append("=").append(value).append(";"));
                httpGet.setHeader("Cookie", cookie.toString());
            }
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
            try (CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet)) {
                requestEntity.setResponse(EntityUtils.toString(closeableHttpResponse.getEntity()));
                requestEntity.setCookies(store.getCookies());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return requestEntity;
    }

    public static RequestEntity get(String url) {
        Map<String, String> cookies = new HashMap<>(0);
        Map<String, String> params = new HashMap<>(0);
        return get(url, params, cookies);
    }
}
