package com.wuyou.landlords.server.event;


import com.wuyou.entity.Player;
import com.wuyou.enums.*;
import com.wuyou.landlords.entity.Room;
import com.wuyou.landlords.helper.PokerHelper;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.client.event.ClientEventListener;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.helper.MapHelper;
import com.wuyou.landlords.server.robot.RobotEventListener;

import java.util.List;
import java.util.Objects;

public class ServerEventListener_CODE_GAME_LANDLORD_ELECT implements ServerEventListener {

    @Override
    public void call(Player player, String data) {
        try {
            player.setStatus(ClientStatus.WAIT);
            Room room = GlobalVariable.getRoom(player.getRoomId());

            if (room != null) {
                boolean isY = Boolean.parseBoolean(data);
                System.out.println(player.getId() + "选择了" + isY);
                if (isY) {
                    player.getPokers().addAll(room.getLandlordPokers());
                    PokerHelper.sortPoker(player.getPokers());

                    String currentClientId = player.getId();
                    room.setLandlordId(currentClientId);
                    room.setLastSellClient(currentClientId);
                    room.setCurrentSellClient(currentClientId);
                    player.setType(ClientType.地主);

                    for (Player client : room.getPlayerList()) {
                        client.setStatus(ClientStatus.PLAYING);
                        GlobalVariable.THREAD_POOL.execute(() -> {
                            String result = MapHelper.newInstance()
                                    .put("roomId", room.getId())
                                    .put("roomOwner", room.getRoomOwner())
                                    .put("roomClientCount", room.getPlayerList().size())
                                    .put("landlordNickname", player.getNickname())
                                    .put("landlordId", player.getId())
                                    .put("additionalPokers", room.getLandlordPokers())
                                    .json();

///						ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_LANDLORD_CONFIRM, result);
                                ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_CONFIRM).call(client, result);
                        });
                    }

                    String landlordNickname = player.getNickname();
                    SenderUtil.sendGroupMsg(room.getId(), landlordNickname + " 已经成为地主并获得了额外的三张牌");
                    List<Poker> additionalPokers = room.getLandlordPokers();
                    SenderUtil.sendGroupMsg(room.getId(), CQ.getPoker(additionalPokers));
                    Thread.sleep(1000);
                    SenderUtil.sendGroupMsg(room.getId(), "下一位玩家是 " + GlobalVariable.LANDLORDS_PLAYER.get(room.getCurrentSellClient()).getNickname() + ", 请等待他确认.");
//                    notifyWatcherConfirmLandlord(room, player);
                } else {
                    if (player.getNext().getId().equals(room.getFirstSellClient())) {
                        for (Player client : room.getPlayerList()) {
                            GlobalVariable.THREAD_POOL.execute(() -> {
                                if (client.getRole() == ClientRole.PLAYER) {
///							ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_LANDLORD_CYCLE, null);
                                    ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_CYCLE).call(client, null);
                                }
                            });
                        }
                        ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(player, null);
                    } else {
                        Player turnPlayer = player.getNext();
                        room.setCurrentSellClient(turnPlayer.getId());
                        String result = MapHelper.newInstance()
                                .put("roomId", room.getId())
                                .put("roomOwner", room.getRoomOwner())
                                .put("roomClientCount", room.getPlayerList().size())
                                .put("preClientNickname", player.getNickname())
                                .put("nextClientNickname", turnPlayer.getNickname())
                                .put("nextClientId", turnPlayer.getId())
                                .json();
                        SenderUtil.sendGroupMsg(room.getId(), player.getNickname() + " 没有抢地主!");
                        SenderUtil.sendGroupMsg(room.getId(), "现在轮到" + turnPlayer.getNickname() + ". 请耐心等待他/她选择是否抢地主!");

                        for (Player client : room.getPlayerList()) {
                            GlobalVariable.THREAD_POOL.execute(() -> {
                                if (client.getRole() == ClientRole.PLAYER) {
///							ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_LANDLORD_ELECT, result);
                                    ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(client, result);

                                } else {
                                    if (client.getId().equals(turnPlayer.getId())) {
                                        Objects.requireNonNull(RobotEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT)).call(client, result);
                                    }
                                }
                            });
                        }

                        notifyWatcherRobLandlord(room, player);
                    }
                }
            }
///            else {
///			ChannelUtils.pushToClient(player, ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
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
    private void notifyWatcherConfirmLandlord(Room room, Player landlord) {
        String json = MapHelper.newInstance()
                .put("landlord", landlord.getNickname())
                .put("additionalPokers", room.getLandlordPokers())
                .json();

        for (Player watcher : room.getWatcherList()) {
///			ChannelUtils.pushToClient(watcher, ClientEventCode.CODE_GAME_LANDLORD_CONFIRM, json);
            ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_CONFIRM).call(watcher, json);

        }
    }

    /**
     * 通知房间内的观战人员抢地主情况
     *
     * @param room 房间
     */
    private void notifyWatcherRobLandlord(Room room, Player player) {
        for (Player watcher : room.getWatcherList()) {
///			ChannelUtils.pushToClient(watcher, ClientEventCode.CODE_GAME_LANDLORD_ELECT, player.getNickname());
            ClientEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(watcher, player.getNickname());

        }
    }
}
