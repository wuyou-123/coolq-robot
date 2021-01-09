package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_SUCCESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		initLastSellInfo();
		
		String joinClientId = (String) map.get("clientId");
		if(qq.equals(joinClientId)) {
			SimplePrinter.sendNotice(qq, "你已经加入房间：" + map.get("roomId") + ". 现在已经有 " + map.get("roomClientCount") + " 位玩家在房间中.");
			SimplePrinter.sendNotice(qq, "请等待其他玩家加入。游戏将在房间满三个玩家时开始!");
		}else {
			SimplePrinter.sendNotice(qq, map.get("clientNickname") + " 加入房间，房间内目前有 " + map.get("roomClientCount") + " 位玩家.");
		}
		
	}



}
