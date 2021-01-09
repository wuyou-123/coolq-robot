package com.wuyou.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import org.nico.ratel.landlords.entity.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator<br>
 * 2020年5月2日
 */
public class GlobalVariable {
    public static final Map<String, Boolean> BOOT_MAP;
    public static final Map<String, String> WEB_COOKIE;
    public static final Map<String, Map<String, Object>> GROUP_DRAGON;
    public static final List<String> ADMINISTRATOR;
    public static final StringBuilder KOUZI;
    public static final ExecutorService THREAD_POOL;
    public static ServerBootstrap BOOTSTRAP = null;
    public static EventLoopGroup PARENT_GROUP = null;
    public static EventLoopGroup CHILD_GROUP = null;
    public static final Map<String, Channel> LANDLORDS_PLAYER;
    public static final Map<String, Room> LANDLORDS_ROOM;
    public static final AttributeKey<Integer> ATTRIBUTE_KEY_NUMBER;
    public static final Map<String, String> CLIENT_ID_MAP;

    static {
        ATTRIBUTE_KEY_NUMBER = AttributeKey.newInstance("name");
        CLIENT_ID_MAP = new HashMap<>();
        BOOT_MAP = new HashMap<>();
        ADMINISTRATOR = new ArrayList<>();
        GROUP_DRAGON = new HashMap<>(64);
        WEB_COOKIE = new HashMap<>(64);
        LANDLORDS_PLAYER = new HashMap<>(128);
        LANDLORDS_ROOM = new HashMap<>(128);
        KOUZI = new StringBuilder();
//        SETU_QUEUE = new LinkedBlockingQueue<>(5);
//        SETU_R18_QUEUE = new LinkedBlockingQueue<>(5);
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 200, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName("newThread" + thread.getId());
            return thread;
        });
    }
//    public static String menu;
//    public static final BlockingQueue<String> SETU_QUEUE;
//    public static final BlockingQueue<String> SETU_R18_QUEUE;
///    public static Map<String, String> face;


}
