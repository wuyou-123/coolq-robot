package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.server.event.ServerEventListener;

/**
 * @author wuyou
 */
public class ClientEventListener_CODE_GAME_POKER_PLAY_CANT_PASS extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		if(player.get("count")!=null){
			player.put("count", player.getData().get("count")+1);
		}else{
			player.put("count", "1");
		}
		SenderUtil.sendPrivateMsg(qq, "不允许不出牌!");
		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(player, null);
//		pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
