package org.nico.ratel.landlords.server;

import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerContains {

    /**
     * The list of client side
     */
    public final static Map<String, ClientSide> CLIENT_SIDE_MAP = new ConcurrentSkipListMap<>();
    public final static Map<String, Integer> CHANNEL_ID_MAP = new ConcurrentHashMap<>();
    public final static ThreadPoolExecutor THREAD_EXCUTER = new ThreadPoolExecutor(500, 500, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), r -> {
                Thread thread = new Thread(r);
                thread.setName("landlords-" + thread.getId());
                return thread;
            });
    /**
     * The map of server side
     */
    private final static Map<Integer, Room> ROOM_MAP = new ConcurrentSkipListMap<>();
    private final static AtomicInteger CLIENT_ATOMIC_ID = new AtomicInteger(1);

    private final static AtomicInteger SERVER_ATOMIC_ID = new AtomicInteger(1);
    /**
     * Server port
     */
    public static int port = 1028;

    public final static int getClientId() {
        return CLIENT_ATOMIC_ID.getAndIncrement();
    }

    public final static int getServerId() {
        return SERVER_ATOMIC_ID.getAndIncrement();
    }

    /**
     * Get room by id, with flush time
     *
     * @param id room id
     * @return
     */
    public final static Room getRoom(int id) {
        Room room = ROOM_MAP.get(id);
        if (room != null) {
            room.setLastFlushTime(System.currentTimeMillis());
        }
        return room;
    }

    public final static Map<Integer, Room> getRoomMap() {
        return ROOM_MAP;
    }

    public final static Room removeRoom(int id) {
        return ROOM_MAP.remove(id);
    }

    public final static Room addRoom(Room room) {
        return ROOM_MAP.put(room.getId(), room);
    }
}
