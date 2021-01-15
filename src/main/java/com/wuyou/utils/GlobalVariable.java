package com.wuyou.utils;

import com.wuyou.entity.Player;
import love.forte.simbot.bot.BotManager;
import com.wuyou.landlords.entity.Room;

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
    public static final Map<String, Boolean> BOOT_MAP;
    public static final Map<String, String> WEB_COOKIE;
    public static final Map<String, Map<String, Object>> GROUP_DRAGON;
    public static final List<String> ADMINISTRATOR;
    public static final StringBuilder KOUZI;
    public static final ExecutorService THREAD_POOL;
    public static final Map<String, Player> LANDLORDS_PLAYER;
    public static final Map<String, Room> LANDLORDS_ROOM;
    public static final Map<String, LinkedBlockingQueue<String>> USER_INPUT;
    private static final Map<String, Room> ROOM_MAP;
    public static BotManager botManager;

    static {
        BOOT_MAP = new HashMap<>();
        ROOM_MAP = new ConcurrentSkipListMap<>();
        ADMINISTRATOR = new ArrayList<>();
        GROUP_DRAGON = new HashMap<>(64);
        WEB_COOKIE = new HashMap<>(64);
        LANDLORDS_PLAYER = new HashMap<>(128);
        LANDLORDS_ROOM = new HashMap<>(128);
        KOUZI = new StringBuilder();
        USER_INPUT = new HashMap<>();
///        SETU_QUEUE = new LinkedBlockingQueue<>(5);
///        SETU_R18_QUEUE = new LinkedBlockingQueue<>(5);
        THREAD_POOL = new ThreadPoolExecutor(50, 50, 200, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), r -> {
            Thread thread = new Thread(r);
            thread.setName("newThread" + thread.getId());
            return thread;
        });
    }

    public static Room getRoom(String id) {
        Room room = ROOM_MAP.get(id);
        if (room != null) {
            room.setLastFlushTime(System.currentTimeMillis());
        }
        return room;
    }

    public static Map<String, Room> getRoomMap() {
        return ROOM_MAP;
    }

    public static Room getRoomById(String qq) {
        if(GlobalVariable.LANDLORDS_PLAYER.get(qq)==null){
            return null;
        }
        return ROOM_MAP.get(GlobalVariable.LANDLORDS_PLAYER.get(qq).getRoomId());
    }

    public static void removeRoom(String id) {
        ROOM_MAP.remove(id);
    }

    public static void addRoom(Room room) {
        ROOM_MAP.put(room.getId(), room);
    }

    public static void resetLandlords() {
        LANDLORDS_PLAYER.clear();
        LANDLORDS_ROOM.clear();
        ROOM_MAP.clear();
        USER_INPUT.clear();
    }
///    public static String menu;
///    public static final BlockingQueue<String> SETU_QUEUE;
///    public static final BlockingQueue<String> SETU_R18_QUEUE;
///    public static Map<String, String> face;

}
