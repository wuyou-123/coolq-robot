package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.landlords.entity.Room;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;

public class ServerEventListener_CODE_CLIENT_READY implements ServerEventListener {

    @Override
    public void call(Player player, String data) {
        String qq = player.getId();
        Room room = GlobalVariable.getRoom(player.getRoomId());
        player.setStatus(ClientStatus.READY);
        long readyCount = room.getPlayerList().stream().filter(item -> item.getStatus() == ClientStatus.READY).count();
        SenderUtil.sendPrivateMsg(qq, "已准备, 现在已经有" + readyCount + "人准备, 所有人准备即可开始游戏");
        SenderUtil.sendPrivateMsg(room.getId(), player.getNickname() + "已准备, 现在已经有" + readyCount + "人准备, 所有人准备即可开始游戏");
        if(readyCount==3) {
            ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(player, String.valueOf(room.getId()));
        }

    }

}
