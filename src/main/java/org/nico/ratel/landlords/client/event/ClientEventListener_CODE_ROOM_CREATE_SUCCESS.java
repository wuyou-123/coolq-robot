package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_ROOM_CREATE_SUCCESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
//		pushToServer(channel, ServerEventCode.CODE_SEND_TO_QQ, "CODE_ROOM_CREATE_SUCCESS");
		Room room = Noson.convert(data, Room.class);
		initLastSellInfo();
		SimplePrinter.sendNotice(qq, "创建房间成功, 房间id: " + room.getId());
		SimplePrinter.sendNotice(qq, "请等待其他玩家加入!");
	}

}
