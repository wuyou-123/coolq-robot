package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_CLIENT_KICK extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		
		SimplePrinter.sendNotice(qq, "你因无所事事而被赶出房间。\n");
		
		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}

}
