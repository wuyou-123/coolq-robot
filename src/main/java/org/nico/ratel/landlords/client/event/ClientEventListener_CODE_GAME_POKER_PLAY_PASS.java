package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_PASS extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		Map<String, Object> map = MapHelper.parser(data);
		
		SenderUtil.sendPrivateMsg(qq, map.get("clientNickname") + " 跳过了. 现在轮到" + map.get("nextClientNickname") + "出牌.");
//		SenderUtil.sendGroupMsg(Objects.requireNonNull(GlobalVariable.getRoomById(qq)).getId(), map.get("clientNickname") + " 跳过了. 现在轮到" + map.get("nextClientNickname") + "出牌.");

		String turnClientId = (String) map.get("nextClientId");
		if(qq.equals(turnClientId)) {
			ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(player, null);
//			pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
		}
	}

}
