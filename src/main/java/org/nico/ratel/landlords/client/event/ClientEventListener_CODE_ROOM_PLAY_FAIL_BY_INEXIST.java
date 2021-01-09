package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_ROOM_PLAY_FAIL_BY_INEXIST extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		
		SimplePrinter.sendNotice(qq, "出牌失败! 房间已解散!");
		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}



}
