package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.SenderUtil;

public class ClientEventListener_CODE_GAME_POKER_PLAY_ORDER_ERROR extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		
		SenderUtil.sendPrivateMsg(qq, "现在不是你的回合,请等待其他玩家出牌!");
	}

}
