package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.helper.MapHelper;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_INEXIST extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> dataMap = MapHelper.parser(data);
		
		SimplePrinter.sendNotice(qq, "加入房间失败! 房间 " + dataMap.get("roomId") + " 不存在!");
//		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}



}
