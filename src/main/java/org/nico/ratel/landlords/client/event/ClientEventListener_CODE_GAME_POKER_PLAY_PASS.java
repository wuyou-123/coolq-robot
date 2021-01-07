package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_PASS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.sendNotice(qq, map.get("clientNickname") + " passed. It is now " + map.get("nextClientNickname") + "'s turn.");
		
		int turnClientId = (int) map.get("nextClientId");
		if(SimpleClient.id == turnClientId) {
			pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
		}
	}

}
