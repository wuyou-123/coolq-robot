package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.server.event.ServerEventListener;

public class ClientEventListener_CODE_GAME_POKER_PLAY_LESS extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		SenderUtil.sendPrivateMsg(qq, "你出的牌比之前的牌小, 不能出这副牌");
		
		if(lastPokers != null) {
			SenderUtil.sendPrivateMsg(qq, lastSellClientNickname + "[" + lastSellClientType + "] 出牌:");
			SenderUtil.sendPrivateMsg(qq, CQ.getPoker(lastPokers));
		}

		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(player, null);
//		pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
