package org.nico.ratel.landlords.server;

import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerContains {

    /**
     * The list of client side
     */
    public final static Map<String, ClientSide> CLIENT_SIDE_MAP = new ConcurrentSkipListMap<>();
    public final static Map<String, String> CHANNEL_ID_MAP = new ConcurrentHashMap<>();
    public final static ThreadPoolExecutor THREAD_EXCUTER = new ThreadPoolExecutor(500, 500, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), r -> {
        Thread thread = new Thread(r);
        thread.setName("landlords-" + thread.getId());
        return thread;
    });
    public final static Map<String, LinkedBlockingQueue<String>> USER_INPUT = new HashMap<>();
    /**
     * The map of server side
     */
    private final static Map<String, Room> ROOM_MAP = new ConcurrentSkipListMap<>();
    private final static AtomicInteger CLIENT_ATOMIC_ID = new AtomicInteger(1);

    private final static AtomicInteger SERVER_ATOMIC_ID = new AtomicInteger(1);
    /**
     * Server port
     */
    public static int port = 1028;

    public static int getClientId() {
        return CLIENT_ATOMIC_ID.getAndIncrement();
    }

    public static int getServerId() {
        return SERVER_ATOMIC_ID.getAndIncrement();
    }

    /**
     * Get room by id, with flush time
     *
     * @param id room id
     * @return room Object
     */
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

    public static void removeRoom(String id) {
        ROOM_MAP.remove(id);
    }

    public static void addRoom(Room room) {
        ROOM_MAP.put(room.getId(), room);
    }

    public static void clearAll() {
        System.out.println("清空");
        ROOM_MAP.clear();
        CLIENT_ATOMIC_ID.set(1);
        SERVER_ATOMIC_ID.set(1);
        CLIENT_SIDE_MAP.clear();
        CHANNEL_ID_MAP.clear();
    }
}
