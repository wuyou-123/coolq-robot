package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_FULL extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> dataMap = MapHelper.parser(data);
//		pushToServer(channel, ServerEventCode.CODE_SEND_TO_QQ, "加入房间失败! 房间 " + dataMap.get("roomId") + " 已满!");

		SimplePrinter.sendNotice(qq, "加入房间失败! 房间 " + dataMap.get("roomId") + " 已满!");
//		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}



}
