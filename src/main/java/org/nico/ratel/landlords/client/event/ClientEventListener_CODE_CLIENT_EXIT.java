package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_EXIT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		System.out.println(qq+"掉线了");
		Map<String, Object> map = MapHelper.parser(data);
		
		Integer exitClientId = (Integer) map.get("exitClientId");
		String role = null;
		if(exitClientId == SimpleClient.id) {
			role = "你";
		}else {
			role = String.valueOf(map.get("exitClientNickname"));
		}
		SimplePrinter.sendNotice(qq, role + " 离开了房间, 房间解散!");

		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}

}
