package com.wuyou.entity;

import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * @author wuyou
 */
public class RequestEntity {
    List<Cookie> cookies;
    String response;

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "cookies=" + cookies +
                ", response='" + response + '\'' +
                '}';
    }
}
