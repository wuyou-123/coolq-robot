package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_GAME_POKER_PLAY_ORDER_ERROR extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		
		SimplePrinter.sendNotice(qq, "It is not your turn yet. Please wait for other players!");
	}

}
