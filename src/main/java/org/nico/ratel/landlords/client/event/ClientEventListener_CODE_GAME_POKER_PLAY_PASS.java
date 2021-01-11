package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_PASS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.sendNotice(qq, map.get("clientNickname") + " 跳过了. 现在轮到" + map.get("nextClientNickname") + "出牌.");
		
		String turnClientId = (String) map.get("nextClientId");
		if(qq.equals(turnClientId)) {
			ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(GetQQUtils.getClient(channel), null);
//			pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
		}
	}

}
