package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_GAME_POKER_PLAY_LESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		SimplePrinter.sendNotice(qq, "Your combination has lower rank than the previous. You cannot play this combination!");
		
		if(lastPokers != null) {
			SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] played:");
			SimplePrinter.printPokers(qq, lastPokers);
		}
		
		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
