package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.print.SimpleWriter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_ELECT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		int turnClientId = (int) map.get("nextClientId");
		
		if(map.containsKey("preClientNickname")) {
			SimplePrinter.sendNotice(qq, map.get("preClientNickname") + " 没有抢地主!");
		}
		
		if(turnClientId == SimpleClient.id) {
			SimplePrinter.sendNotice(qq, "现在轮到你了. 你是否要抢地主? [Y/N] (输入 [exit|e] 离开当前房间)");
			String line = SimpleWriter.write("Y/N");
			if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")) {
				pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT);
			}else if(line.equalsIgnoreCase("Y")){
				pushToServer(channel, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "TRUE");
			}else if(line.equalsIgnoreCase("N")){
				pushToServer(channel, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "FALSE");
			}else{
				SimplePrinter.sendNotice(qq, "无效的输入");
				call(channel, data);
			}
		}else {
			SimplePrinter.sendNotice(qq, "现在轮到" + map.get("nextClientNickname") + ". 请耐心等待他/她出牌!");
		}
		
	}

}
