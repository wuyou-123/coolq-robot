package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.landlords.entity.Room;
import com.wuyou.utils.SenderUtil;
import org.nico.noson.Noson;

public class ClientEventListener_CODE_ROOM_CREATE_SUCCESS extends ClientEventListener {

    @Override
    public void call(Player player, String data) {
        String qq = player.getId();
        Room room = Noson.convert(data, Room.class);
        initLastSellInfo();
        SenderUtil.sendGroupMsg(room.getId(), "创建房间成功! 等待玩家加入");
        SenderUtil.sendPrivateMsg(qq, "创建房间成功, 房间id: " + room.getId());
        SenderUtil.sendPrivateMsg(qq, "请等待其他玩家加入!");
    }

}
