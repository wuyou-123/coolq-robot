package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_GAME_LANDLORD_CYCLE extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		SimplePrinter.sendNotice(qq, "没有玩家抢地主, 重新发牌");
		
	}

}
