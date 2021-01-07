package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.FormatPrinter;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_REDIRECT extends ClientEventListener{

	private static String[] choose = new String[]{"UP", "DOWN"};
	
	private static String format = "\n[%-4s] %-" + 15 + "s  surplus %-2s [%-8s]";
	
	@SuppressWarnings("unchecked")
	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		
		Map<String, Object> map = MapHelper.parser(data);
		
		int sellClientId = (int) map.get("sellClientId");
		
		List<Map<String, Object>> clientInfos = (List<Map<String, Object>>) map.get("clientInfos");
		
		for(int index = 0; index < 2; index ++){
			for(Map<String, Object> clientInfo: clientInfos) {
				String position = (String) clientInfo.get("position");
				if(position.equalsIgnoreCase(choose[index])){
					FormatPrinter.printNotice(format, clientInfo.get("position"), clientInfo.get("clientNickname"), clientInfo.get("surplus"), clientInfo.get("type"));
				}
			}
		}
		SimplePrinter.sendNotice(qq, "");
		
		if(sellClientId == SimpleClient.id) {
			get(ClientEventCode.CODE_GAME_POKER_PLAY).call(channel, data);
		}else {
			SimplePrinter.sendNotice(qq, "It is " + map.get("sellClinetNickname") + "'s turn. Please wait for him to play his cards.");
		}
	}

}
