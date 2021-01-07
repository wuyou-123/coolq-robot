package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_SHOW_POKERS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		
		Map<String, Object> map = MapHelper.parser(data);
		
		lastSellClientNickname = (String) map.get("clientNickname");
		lastSellClientType = (String) map.get("clientType");
		
		SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] 出牌:");
		lastPokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});
		SimplePrinter.printPokers(qq, lastPokers);
		
		if(map.containsKey("sellClinetNickname")) {
			SimplePrinter.sendNotice(qq, "下一位玩家是 " + map.get("sellClinetNickname") + ", 请等待他确认.");
		}
	}

}
