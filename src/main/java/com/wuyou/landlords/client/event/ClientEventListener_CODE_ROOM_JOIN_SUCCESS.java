package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.helper.MapHelper;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_SUCCESS extends ClientEventListener {

    @Override
    public void call(Player player, String data) {
        String qq = player.getId();
        Map<String, Object> map = MapHelper.parser(data);
        initLastSellInfo();

        String joinClientId = (String) map.get("clientId");
        if (qq.equals(joinClientId)) {
            SenderUtil.sendPrivateMsg(qq, "你已经加入房间：" + map.get("roomId") + ". 现在已经有 " + map.get("roomClientCount") + " 位玩家在房间中.");
            SenderUtil.sendPrivateMsg(qq, "请等待其他玩家加入。游戏将在房间满三个玩家时开始!");
        } else {
            SenderUtil.sendPrivateMsg(qq, map.get("clientNickname") + " 加入房间，房间内目前有 " + map.get("roomClientCount") + " 位玩家.");
        }

    }


}
