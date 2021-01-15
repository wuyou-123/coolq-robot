package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.helper.MapHelper;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_INEXIST extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		Map<String, Object> dataMap = MapHelper.parser(data);
		
		SenderUtil.sendPrivateMsg(qq, "加入房间失败! 房间 " + dataMap.get("roomId") + " 不存在!");
//		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(player, data);
	}



}
