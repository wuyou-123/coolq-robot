package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_MISMATCH extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.sendNotice(qq, "Your combination is " + map.get("playType") + " (" + map.get("playCount") + "), but the previous combination is " + map.get("preType") + " (" + map.get("preCount") + "). Mismatch!");
		
		if(lastPokers != null) {
			SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] played:");
			SimplePrinter.printPokers(qq, lastPokers);
		}
		
		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
