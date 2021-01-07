package org.nico.ratel.landlords.server.event;

import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.enums.ClientRole;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_OFFLINE implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {

        Room room = ServerContains.getRoom(clientSide.getRoomId());

        if (room != null) {
            String result = MapHelper.newInstance()
                    .put("roomId", room.getId())
                    .put("exitClientId", clientSide.getId())
                    .put("exitClientNickname", clientSide.getNickname())
                    .json();
            for (ClientSide client : room.getClientSideList()) {
                if (client.getRole() == ClientRole.PLAYER) {
                    if (client.getId() != clientSide.getId()) {
//						ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, result);
                        ClientEventListener.get(ClientEventCode.CODE_CLIENT_EXIT).call(client.getChannel(), result);

                        client.init();
                    }
                }
            }
            ServerContains.removeRoom(room.getId());
        }
        System.out.println("掉线");
//        try {
            throw new RuntimeException("111111");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try {
//            ServerContains.CLIENT_SIDE_MAP.remove(GlobalVariable.CLIENT_ID_MAP.get(clientSide.getId()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
