package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.SenderUtil;

public class ClientEventListener_CODE_GAME_LANDLORD_CYCLE extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		SenderUtil.sendPrivateMsg(qq, "没有玩家抢地主, 重新发牌");
		
	}

}
