package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.enums.ClientRole;
import com.wuyou.landlords.entity.Room;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.landlords.client.event.ClientEventListener;
import com.wuyou.landlords.helper.MapHelper;

public class ServerEventListener_CODE_CLIENT_OFFLINE implements ServerEventListener {

    @Override
    public void call(Player player, String data) {

        Room room = GlobalVariable.getRoom(player.getRoomId());

        if (room != null) {
            String result = MapHelper.newInstance()
                    .put("roomId", room.getId())
                    .put("exitClientId", player.getId())
                    .put("exitClientNickname", player.getNickname())
                    .json();
            for (Player client : room.getPlayerList()) {
                if (client.getRole() == ClientRole.PLAYER) {
                    if (!client.getId().equals(player.getId())) {
//						ChannelUtils.pushToClient(player, ClientEventCode.CODE_CLIENT_EXIT, result);
                        ClientEventListener.get(ClientEventCode.CODE_CLIENT_EXIT).call(client, result);

                        client.init();
                    }
                }
            }
            GlobalVariable.removeRoom(room.getId());
        }
        System.out.println("掉线");
//        try {
            throw new RuntimeException("111111");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try {
//            ServerContains.CLIENT_SIDE_MAP.remove(GlobalVariable.CLIENT_ID_MAP.get(player.getId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
