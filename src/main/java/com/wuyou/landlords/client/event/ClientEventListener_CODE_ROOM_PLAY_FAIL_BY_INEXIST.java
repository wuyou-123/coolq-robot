package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.SenderUtil;

public class ClientEventListener_CODE_ROOM_PLAY_FAIL_BY_INEXIST extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		
		SenderUtil.sendPrivateMsg(qq, "出牌失败! 房间已解散!");
		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(player, data);
	}



}
