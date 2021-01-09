package org.nico.ratel.landlords.server.event;


import com.wuyou.utils.GlobalVariable;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.enums.ClientRole;
import org.nico.ratel.landlords.enums.ClientType;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.helper.PokerHelper;
import org.nico.ratel.landlords.server.ServerContains;
import org.nico.ratel.landlords.server.robot.RobotEventListener;

public class ServerEventListener_CODE_GAME_LANDLORD_ELECT implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {
        try {
            Room room = ServerContains.getRoom(clientSide.getRoomId());

            if (room != null) {
                boolean isY = Boolean.parseBoolean(data);
                System.out.println(clientSide.getId() + "选择了" + isY);
                if (isY) {
                    clientSide.getPokers().addAll(room.getLandlordPokers());
                    PokerHelper.sortPoker(clientSide.getPokers());

                    String currentClientId = clientSide.getId();
                    room.setLandlordId(currentClientId);
                    room.setLastSellClient(currentClientId);
                    room.setCurrentSellClient(currentClientId);
                    clientSide.setType(ClientType.地主);

                    for (ClientSide client : room.getClientSideList()) {
                        GlobalVariable.THREAD_POOL.execute(() -> {
                            System.out.println(room.getClientSideList().size());
                            System.out.println("11111-------" + client.getId());
                            String result = MapHelper.newInstance()
                                    .put("roomId", room.getId())
                                    .put("roomOwner", room.getRoomOwner())
                                    .put("roomClientCount", room.getClientSideList().size())
                                    .put("landlordNickname", clientSide.getNickname())
                                    .put("landlordId", clientSide.getId())
                                    .put("additionalPokers", room.getLandlordPokers())
                                    .json();

                            if (client.getRole() == ClientRole.PLAYER) {
///						ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_CONFIRM, result);
                                ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_CONFIRM).call(client.getChannel(), result);

                            } else {
                                if (currentClientId.equals(client.getId())) {
                                    RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(client, result);
                                }
                            }
                        });
                    }

                    notifyWatcherConfirmLandlord(room, clientSide);
                } else {
                    if (clientSide.getNext().getId().equals(room.getFirstSellClient())) {
                        for (ClientSide client : room.getClientSideList()) {
                            GlobalVariable.THREAD_POOL.execute(() -> {
                                if (client.getRole() == ClientRole.PLAYER) {
///							ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_CYCLE, null);
                                    ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_CYCLE).call(client.getChannel(), null);
                                }
                            });
                        }

                        ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(clientSide, null);
                    } else {
                        ClientSide turnClientSide = clientSide.getNext();
                        room.setCurrentSellClient(turnClientSide.getId());
                        String result = MapHelper.newInstance()
                                .put("roomId", room.getId())
                                .put("roomOwner", room.getRoomOwner())
                                .put("roomClientCount", room.getClientSideList().size())
                                .put("preClientNickname", clientSide.getNickname())
                                .put("nextClientNickname", turnClientSide.getNickname())
                                .put("nextClientId", turnClientSide.getId())
                                .json();

                        for (ClientSide client : room.getClientSideList()) {
                            GlobalVariable.THREAD_POOL.execute(() -> {
                                if (client.getRole() == ClientRole.PLAYER) {
///							ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_ELECT, result);
                                    ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(client.getChannel(), result);

                                } else {
                                    if (client.getId().equals(turnClientSide.getId())) {
                                        RobotEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(client, result);
                                    }
                                }
                            });
                        }

                        notifyWatcherRobLandlord(room, clientSide);
                    }
                }
            }
///            else {
///			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
///            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知房间内的观战人员谁是地主
     *
     * @param room     房间
     * @param landlord 地主
     */
    private void notifyWatcherConfirmLandlord(Room room, ClientSide landlord) {
        String json = MapHelper.newInstance()
                .put("landlord", landlord.getNickname())
                .put("additionalPokers", room.getLandlordPokers())
                .json();

        for (ClientSide watcher : room.getWatcherList()) {
///			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_CONFIRM, json);
            ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_CONFIRM).call(watcher.getChannel(), json);

        }
    }

    /**
     * 通知房间内的观战人员抢地主情况
     *
     * @param room 房间
     */
    private void notifyWatcherRobLandlord(Room room, ClientSide player) {
        for (ClientSide watcher : room.getWatcherList()) {
///			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_ELECT, player.getNickname());
            ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(watcher.getChannel(), player.getNickname());

        }
    }
}
