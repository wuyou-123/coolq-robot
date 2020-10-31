package com.wuyou.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Administrator<br>
 * 2020年5月2日
 */
public class GlobalVariable {
    static {
        BOOT_MAP = new HashMap<>();
        ADMINISTRATOR = new ArrayList<>();
        GROUP_DRAGON = new HashMap<>(64);
        WEB_COOKIE = new HashMap<>(64);
        KOUZI = new StringBuilder();
//        SETU_QUEUE = new LinkedBlockingQueue<>(5);
//        SETU_R18_QUEUE = new LinkedBlockingQueue<>(5);
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 200, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName("newThread" + thread.getId());
            return thread;
        });
    }

    public static final Map<String, Boolean> BOOT_MAP;
    public static final Map<String, String> WEB_COOKIE;
    public static final Map<String, Map<String, Object>> GROUP_DRAGON;
    public static final List<String> ADMINISTRATOR;
    public static final StringBuilder KOUZI;
    public static final ExecutorService THREAD_POOL;
//    public static String menu;
//    public static final BlockingQueue<String> SETU_QUEUE;
//    public static final BlockingQueue<String> SETU_R18_QUEUE;
///    public static Map<String, String> face;


}
