package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_EXIT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		System.out.println(qq+"掉线了");
		Map<String, Object> map = MapHelper.parser(data);
		GlobalVariable.LANDLORDS_PLAYER.remove(qq);
		String exitClientId = (String) map.get("exitClientId");
		String role;
		if(exitClientId.equals(qq)) {
			role = "你";
		}else {
			role = String.valueOf(map.get("exitClientNickname"));
		}
		SimplePrinter.sendNotice(qq, role + " 离开了房间, 房间解散!");

		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}

}
