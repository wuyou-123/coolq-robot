package org.nico.ratel.landlords.server.timer;

import org.nico.ratel.landlords.channel.ChannelUtils;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.enums.*;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.server.ServerContains;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.server.robot.RobotEventListener;

import java.util.Map;
import java.util.TimerTask;

/**
 * @author nico
 */

public class RoomClearTask extends TimerTask {

    //The room wait time of after create is 100s
    private static long waitingStatusInterval = 1000 * 100;

    //The room starting destroy time is 100s
    private static long startingStatusInterval = 1000 * 100;

    //The room live  time is 600s
    private static long liveTime = 1000 * 60 * 20;

    @Override
    public void run() {
        try {
            doing();
        } catch (Exception e) {
            SimplePrinter.serverLog(e.getMessage());
        }

    }

    public void doing() {
        Map<String, Room> rooms = ServerContains.getRoomMap();
        if (rooms != null && !rooms.isEmpty()) {
            long now = System.currentTimeMillis();
            for (Room room : rooms.values()) {
                long alreadyLiveTime = System.currentTimeMillis() - room.getCreateTime();
                SimplePrinter.serverLog("room " + room.getId() + " already live " + alreadyLiveTime + "ms");
                if (alreadyLiveTime > liveTime) {
                    SimplePrinter.serverLog("room " + room.getId() + " live time overflow " + liveTime + ", closed!");
                    ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(room.getClientSideList().get(0), null);
                } else {
                    long diff = now - room.getLastFlushTime();

                    if (room.getStatus() != RoomStatus.STARTING) {
                        long interval = waitingStatusInterval;

                        if (diff > interval) {
                            SimplePrinter.serverLog("room " + room.getId() + " starting waiting time overflow " + waitingStatusInterval + ", closed!");

                            ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(room.getClientSideList().get(0), null);
                        }
                    } else {
                        if (room.getType() == RoomType.PVP) {
                            long interval = startingStatusInterval;

                            if (diff > interval) {
                                boolean allRobots = true;
                                for (ClientSide client : room.getClientSideList()) {
                                    if (!client.getId().equals(room.getCurrentSellClient()) && client.getRole() == ClientRole.PLAYER) {
                                        allRobots = false;
                                        break;
                                    }
                                }

                                ClientSide currentPlayer = room.getClientSideMap().get(room.getCurrentSellClient());

                                if (allRobots) {
                                    SimplePrinter.serverLog("room " + room.getId() + " all is robots, closed!");

                                    ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(currentPlayer, null);
                                } else {
                                    //kick this client
//									ChannelUtils.pushToClient(currentPlayer.getChannel(), ClientEventCode.CODE_CLIENT_KICK, null);
                                    ClientEventListener.get(ClientEventCode.CODE_CLIENT_KICK).call(currentPlayer.getChannel(), null);

                                    notifyWatcherClientKick(room, currentPlayer);

                                    //client current player
                                    room.getClientSideMap().remove(currentPlayer.getId());
                                    room.getClientSideList().remove(currentPlayer);

                                    ClientSide robot = new ClientSide("-"+currentPlayer.getId(), ClientStatus.PLAYING, null);
                                    robot.setNickname(currentPlayer.getNickname());
                                    robot.setRole(ClientRole.ROBOT);
                                    robot.setRoomId(room.getId());
                                    robot.setNext(currentPlayer.getNext());
                                    robot.setPre(currentPlayer.getPre());
                                    robot.getNext().setPre(robot);
                                    robot.getPre().setNext(robot);
                                    robot.setPokers(currentPlayer.getPokers());
                                    robot.setType(currentPlayer.getType());

                                    room.getClientSideMap().put(robot.getId(), robot);
                                    room.getClientSideList().add(robot);
                                    room.setCurrentSellClient(robot.getId());

                                    //If last sell client is current client, replace it to robot id
                                    if (room.getLastSellClient().equals(currentPlayer.getId())) {
                                        room.setLastSellClient(robot.getId());
                                    }


                                    //set robot difficulty -> simple
                                    room.setDifficultyCoefficient(1);

                                    ServerContains.CLIENT_SIDE_MAP.put(robot.getId() + "", robot);

                                    //init client
                                    currentPlayer.init();

                                    SimplePrinter.serverLog("room " + room.getId() + " player " + currentPlayer.getNickname() + " " + startingStatusInterval + "ms not operating, automatic custody!");

                                    if ("-1".equals(room.getLandlordId())) {
                                        RobotEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(robot, null);
                                    } else {
                                        RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(robot, null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 通知观战者玩家被提出房间
     *
     * @param room   房间
     * @param player 被提出的玩家
     */
    private void notifyWatcherClientKick(Room room, ClientSide player) {
        for (ClientSide watcher : room.getWatcherList()) {
            ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_CLIENT_KICK, player.getNickname());
            ClientEventListener.get(ClientEventCode.CODE_CLIENT_KICK).call(watcher.getChannel(), player.getNickname());

        }
    }
}
