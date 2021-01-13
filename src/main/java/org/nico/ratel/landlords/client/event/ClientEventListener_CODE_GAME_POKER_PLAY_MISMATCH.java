package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_MISMATCH extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		Map<String, Object> map = MapHelper.parser(data);
		
		SenderUtil.sendPrivateMsg(qq, "你出的牌是 " + map.get("playType") + " (" + map.get("playCount") + "), 但是之前的牌是" + map.get("preType") + " (" + map.get("preCount") + "). 不匹配!");
		
		if(lastPokers != null) {
			SenderUtil.sendPrivateMsg(qq, lastSellClientNickname + "[" + lastSellClientType + "] 出牌:");
			SenderUtil.sendPrivateMsg(qq, CQ.getPoker(lastPokers));
		}
		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(player, null);
//		pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
