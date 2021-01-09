package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_PVE_DIFFICULTY_NOT_SUPPORT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		SimplePrinter.sendNotice(qq, "The current difficulty is not supported, please pay attention to the following.\n");
		//get(ClientEventCode.CODE_SHOW_OPTIONS_PVE).call(channel, data);
	}



}
