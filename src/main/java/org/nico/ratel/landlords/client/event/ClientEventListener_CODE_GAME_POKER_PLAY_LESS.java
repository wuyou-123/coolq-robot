package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_GAME_POKER_PLAY_LESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		SimplePrinter.sendNotice(qq, "你出的牌比之前的牌小, 不能出这副牌");
		
		if(lastPokers != null) {
			SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] 出牌:");
			SimplePrinter.printPokers(qq, lastPokers);
		}

		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(GetQQUtils.getClient(channel), null);
//		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
