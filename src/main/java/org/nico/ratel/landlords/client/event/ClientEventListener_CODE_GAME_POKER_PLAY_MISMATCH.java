package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_MISMATCH extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.sendNotice(qq, "你出的牌是 " + map.get("playType") + " (" + map.get("playCount") + "), 但是之前的牌是" + map.get("preType") + " (" + map.get("preCount") + "). 不匹配!");
		
		if(lastPokers != null) {
			SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] 出牌:");
			SimplePrinter.printPokers(qq, lastPokers);
		}
		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(GetQQUtils.getClient(channel), null);
//		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
