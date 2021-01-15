package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.enums.ClientRole;
import com.wuyou.enums.ClientType;
import com.wuyou.enums.RoomStatus;
import com.wuyou.landlords.entity.Room;
import com.wuyou.landlords.helper.PokerHelper;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.landlords.client.event.ClientEventListener;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.helper.MapHelper;
import com.wuyou.landlords.server.robot.RobotEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ServerEventListener_CODE_GAME_STARTING implements ServerEventListener {

    @Override
    public void call(Player player, String data) {

        Room room = GlobalVariable.getRoom(player.getRoomId());

        LinkedList<Player> roomClientList = room.getPlayerList();

        // Send the points of poker
        List<List<Poker>> pokersList = PokerHelper.distributePoker();
        int cursor = 0;
        for (Player client : roomClientList) {
            client.setPokers(pokersList.get(cursor++));
        }
        room.setLandlordPokers(pokersList.get(3));

        // Push information about the robber
        int startGrabIndex = (int) (Math.random() * 3);
        Player startGrabClient = roomClientList.get(startGrabIndex);
        room.setCurrentSellClient(startGrabClient.getId());

        // Push start game messages
        room.setStatus(RoomStatus.STARTING);

        // Record the first speaker
        room.setFirstSellClient(startGrabClient.getId());
//        SenderUtil.sendGroupMsg(room.getId(), "游戏已开始!");
        for (Player client : roomClientList) {
            System.out.println(client.getId());
            GlobalVariable.THREAD_POOL.execute(() -> {
                client.setType(ClientType.农民);

                String result = MapHelper.newInstance()
                        .put("roomId", room.getId())
                        .put("roomOwner", room.getRoomOwner())
                        .put("roomClientCount", room.getPlayerList().size())
                        .put("nextClientNickname", startGrabClient.getNickname())
                        .put("nextClientId", startGrabClient.getId())
                        .put("pokers", client.getPokers())
                        // this key-value use to client order to show
                        .put("clientOrderList", roomClientList)
                        .json();

                if (client.getRole() == ClientRole.PLAYER) {
//				ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_STARTING, result);
                    ClientEventListener.get(ClientEventCode.CODE_GAME_STARTING).call(client, result);
                } else {
                    if (startGrabClient.getId().equals(client.getId())) {
                        Objects.requireNonNull(RobotEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT)).call(client, result);
                    }
                }
            });
        }

//        notifyWatcherGameStart(room);

    }


    /**
     * 通知房间内的观战人员游戏开始
     *
     * @param room 房间
     */
    private void notifyWatcherGameStart(Room room) {
        for (Player player : room.getWatcherList()) {
            String result = MapHelper.newInstance()
                    .put("player1", room.getPlayerList().getFirst().getNickname())
                    .put("pokers1", room.getPlayerList().getFirst().getPokers())
                    .put("player2", room.getPlayerList().getFirst().getNext().getNickname())
                    .put("pokers2", room.getPlayerList().getFirst().getNext().getPokers())
                    .put("player3", room.getPlayerList().getLast().getNickname())
                    .put("pokers3", room.getPlayerList().getLast().getPokers())
                    .json();

//			ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_STARTING, result);
            ClientEventListener.get(ClientEventCode.CODE_GAME_STARTING).call(player, result);

        }
    }

}
