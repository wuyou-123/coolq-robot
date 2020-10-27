package com.wuyou.entity;

import lombok.Data;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * 响应实体类
 *
 * @author wuyou
 */
@Data
public class RequestEntity {
    List<Cookie> cookies;
    String response;

}
